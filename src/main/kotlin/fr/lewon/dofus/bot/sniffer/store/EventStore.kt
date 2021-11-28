package fr.lewon.dofus.bot.sniffer.store

import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.locks.ReentrantLock

object EventStore {

    private const val queueSize = 200
    private val queueMapper = HashMap<Long, HashMap<Long, ArrayBlockingQueue<INetworkMessage>>>()
    private val handlerMapper = HashMap<Class<out INetworkMessage>, ArrayList<EventHandler<INetworkMessage>>>()
    private val lock = ReentrantLock()

    fun addSocketEvent(dofusEvent: INetworkMessage, sequenceNumber: Long, snifferId: Long) {
        try {
            lock.lock()
            val eventQueue = queueMapper.computeIfAbsent(snifferId) { HashMap() }
                .computeIfAbsent(sequenceNumber) { ArrayBlockingQueue(queueSize) }
            if (!eventQueue.offer(dofusEvent)) {
                eventQueue.poll()
                eventQueue.offer(dofusEvent)
            }
            handlerMapper[dofusEvent.javaClass]?.forEach {
                it.onEventReceived(dofusEvent, snifferId)
            }
        } finally {
            lock.unlock()
        }
    }

    private fun getEventQueue(snifferId: Long): HashMap<Long, ArrayBlockingQueue<INetworkMessage>> {
        return queueMapper.computeIfAbsent(snifferId) { HashMap() }
    }

    private fun getAllEvents(eventQueue: Map<Long, ArrayBlockingQueue<INetworkMessage>>): List<INetworkMessage> {
        return eventQueue.entries.flatMap { it.value }
    }

    private fun <T : INetworkMessage> getAllEvents(
        eventQueue: Map<Long, ArrayBlockingQueue<INetworkMessage>>,
        eventClass: Class<T>
    ): List<T> {
        return getAllEvents(eventQueue)
            .filter { it::class.java == eventClass }
            .map { eventClass.cast(it) }
    }

    fun getAllEvents(snifferId: Long): List<INetworkMessage> {
        try {
            lock.lock()
            return getAllEvents(getEventQueue(snifferId))
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkMessage> getAllEvents(eventClass: Class<T>, snifferId: Long): List<T> {
        try {
            lock.lock()
            return getAllEvents(getEventQueue(snifferId), eventClass)
        } finally {
            lock.unlock()
        }
    }

    fun removeSequence(
        snifferId: Long,
        mainEventClass: Class<out INetworkMessage>,
        endEventClass: Class<out INetworkMessage>,
    ) {
        try {
            lock.lock()
            val eventQueue = getEventQueue(snifferId)
            val firstIndexes = getFirstIndexes(eventQueue, mainEventClass)
                ?: error("${mainEventClass.simpleName} not found")
            val subQueue = getSubQueue(eventQueue, firstIndexes)
            val lastIndexes = getFirstIndexes(subQueue, endEventClass)
                ?: error("${endEventClass.simpleName} not found")

            val toRemoveFirst = eventQueue[firstIndexes.first]
                ?.withIndex()?.firstOrNull { it.index == firstIndexes.second }?.value
                ?: error("Event with indexes [${firstIndexes.first} ; ${firstIndexes.second}] not found in queue")
            val toRemoveLast = subQueue[lastIndexes.first]
                ?.withIndex()?.firstOrNull { it.index == lastIndexes.second }?.value
                ?: error("Event with indexes [${lastIndexes.first} ; ${lastIndexes.second}] not found in queue")

            eventQueue[firstIndexes.first]?.remove(toRemoveFirst)?.takeIf { it }
                ?: error("Couldn't remove element from queue at position : [${firstIndexes.first} ; ${firstIndexes.second}]")
            eventQueue[lastIndexes.first]?.remove(toRemoveLast)?.takeIf { it }
                ?: error("Couldn't remove element from queue at position : [${lastIndexes.first} ; ${lastIndexes.second}]")
        } finally {
            lock.unlock()
        }
    }

    fun containsSequence(
        snifferId: Long,
        mainEventClass: Class<out INetworkMessage>,
        endEventClass: Class<out INetworkMessage>
    ): Boolean {
        try {
            lock.lock()
            val eventQueue = getEventQueue(snifferId)
            val firstIndexes = getFirstIndexes(eventQueue, mainEventClass) ?: return false
            return getFirstIndexes(getSubQueue(eventQueue, firstIndexes), endEventClass) != null
        } finally {
            lock.unlock()
        }
    }

    private fun getSubQueue(
        eventQueue: Map<Long, ArrayBlockingQueue<INetworkMessage>>,
        fromIndexes: Pair<Long, Int>
    ): Map<Long, ArrayBlockingQueue<INetworkMessage>> {
        val subQueue = HashMap<Long, ArrayBlockingQueue<INetworkMessage>>()
        val firstMsgQueueContent = eventQueue[fromIndexes.first]
            ?.filterIndexed { index, _ -> index > fromIndexes.second }
            ?: emptyList()
        subQueue[fromIndexes.first] = ArrayBlockingQueue<INetworkMessage>(queueSize)
            .also { it.addAll(firstMsgQueueContent) }
        for (entry in eventQueue.filter { it.key > fromIndexes.first }) {
            subQueue[entry.key] = ArrayBlockingQueue<INetworkMessage>(queueSize)
                .also { it.addAll(entry.value) }
        }
        return subQueue
    }

    private fun getFirstIndexes(
        eventQueue: Map<Long, ArrayBlockingQueue<INetworkMessage>>,
        eventClass: Class<out INetworkMessage>
    ): Pair<Long, Int>? {
        for (entry in eventQueue.entries) {
            val indexOfEventClass = entry.value.indexOfFirst { it::class.java == eventClass }
            if (indexOfEventClass >= 0) {
                return Pair(entry.key, indexOfEventClass)
            }
        }
        return null
    }

    fun <T : INetworkMessage> getLastEvent(eventClass: Class<T>, snifferId: Long): T? {
        try {
            lock.lock()
            return getAllEvents(eventClass, snifferId).lastOrNull()
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkMessage> getFirstEvent(eventClass: Class<T>, snifferId: Long): T? {
        try {
            lock.lock()
            return getAllEvents(eventClass, snifferId).firstOrNull()
        } finally {
            lock.unlock()
        }
    }

    fun clear(snifferId: Long) {
        try {
            lock.lock()
            queueMapper[snifferId]?.clear()
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkType> clear(eventClass: Class<T>, snifferId: Long) {
        try {
            lock.lock()
            queueMapper[snifferId]?.values?.forEach {
                it.removeIf { msg -> msg::class.java == eventClass }
            }
        } finally {
            lock.unlock()
        }
    }

    fun getStoredEventsStr(snifferId: Long): String {
        try {
            lock.lock()
            val sb = StringBuilder()
            val eventQueue = getEventQueue(snifferId)
            eventQueue.entries.forEach {
                sb.append("${it.key} :\n")
                for (msg in it.value) {
                    sb.append(" - ${msg::class.java.simpleName}\n")
                }
            }
            return sb.toString()
        } finally {
            lock.unlock()
        }
    }

    @Synchronized
    fun <T : INetworkMessage> addEventHandler(eventClass: Class<T>, eventHandler: EventHandler<T>) {
        val eventHandlers = handlerMapper.computeIfAbsent(eventClass) { ArrayList() }
        eventHandlers.add(eventHandler as EventHandler<INetworkMessage>)
    }

    fun <T : INetworkMessage> addEventHandler(eventHandler: EventHandler<T>) {
        val eventHandlerInterface = getAllGenericInterfaces(eventHandler::class.java)
            .filterIsInstance<ParameterizedType>()
            .firstOrNull { EventHandler::class.java.isAssignableFrom(it.rawType as Class<*>) }
            ?: return
        val actualTypeArgument = eventHandlerInterface.actualTypeArguments[0]
        if (actualTypeArgument is TypeVariable<*>) {
            val argumentClass = actualTypeArgument.bounds[0] as Class<*>
            val realType = getRealType(argumentClass, eventHandler::class.java, EventHandler::class.java)
            addEventHandler(realType as Class<T>, eventHandler)
        } else if (actualTypeArgument is Class<*>) {
            addEventHandler(actualTypeArgument as Class<T>, eventHandler)
        }
    }

    private fun getRealType(typeParameterClass: Class<*>, baseClass: Class<*>, parentClass: Class<*>): Class<*> {
        val genericSuperClass = baseClass.genericSuperclass
        if (genericSuperClass is ParameterizedType) {
            genericSuperClass.actualTypeArguments.firstOrNull {
                it is Class<*> && typeParameterClass.isAssignableFrom(it)
            }?.let { return it as Class<*> }
        }
        if (baseClass.isAssignableFrom(parentClass)) {
            return typeParameterClass
        }
        return getRealType(typeParameterClass, baseClass.superclass, parentClass)
    }

    private fun getAllGenericInterfaces(refClass: Class<*>): Set<Type> {
        val interfaces = HashSet<Type>()
        if (refClass.superclass != null) {
            interfaces.addAll(getAllGenericInterfaces(refClass.superclass))
        }
        interfaces.addAll(refClass.genericInterfaces)
        return interfaces
    }

}