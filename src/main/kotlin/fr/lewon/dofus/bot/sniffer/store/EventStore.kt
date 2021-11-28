package fr.lewon.dofus.bot.sniffer.store

import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.locks.ReentrantLock

object EventStore {

    private const val queueSize = 500
    private val queueMapper = HashMap<Long, ArrayBlockingQueue<INetworkMessage>>()
    private val handlerMapper = HashMap<Class<out INetworkMessage>, ArrayList<EventHandler<INetworkMessage>>>()
    private val lock = ReentrantLock()

    fun addSocketEvent(dofusEvent: INetworkMessage, snifferId: Long) {
        try {
            lock.lock()
            val eventQueue = getEventQueue(snifferId)
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

    private fun getEventQueue(snifferId: Long): ArrayBlockingQueue<INetworkMessage> {
        return queueMapper.computeIfAbsent(snifferId) { ArrayBlockingQueue(queueSize) }
    }

    private fun <T : INetworkMessage> getAllEvents(
        eventQueue: ArrayBlockingQueue<INetworkMessage>,
        eventClass: Class<T>
    ): List<T> {
        return eventQueue
            .filter { it::class.java == eventClass }
            .map { eventClass.cast(it) }
    }

    fun getAllEvents(snifferId: Long): List<INetworkMessage> {
        try {
            lock.lock()
            return getEventQueue(snifferId).toList()
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
            val firstIndex = getFirstIndex(eventQueue, mainEventClass)
                ?: error("${mainEventClass.simpleName} not found")
            val subQueue = getSubQueue(eventQueue, firstIndex)
            val lastIndex = getFirstIndex(subQueue, endEventClass)
                ?: error("${endEventClass.simpleName} not found")

            val toRemoveFirst = eventQueue.withIndex().firstOrNull { it.index == firstIndex }?.value
                ?: error("Event with index [$firstIndex] not found in queue")
            val toRemoveLast = subQueue.withIndex().firstOrNull { it.index == lastIndex }?.value
                ?: error("Event with index [$lastIndex] not found in queue")

            if (!eventQueue.remove(toRemoveFirst)) {
                error("Couldn't remove element from queue at position : [$firstIndex]")
            }
            if (!eventQueue.remove(toRemoveLast)) {
                error("Couldn't remove element from queue at position : [$lastIndex]")
            }
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
            val firstIndex = getFirstIndex(eventQueue, mainEventClass) ?: return false
            return getFirstIndex(getSubQueue(eventQueue, firstIndex), endEventClass) != null
        } finally {
            lock.unlock()
        }
    }

    private fun getSubQueue(
        eventQueue: ArrayBlockingQueue<INetworkMessage>,
        fromIndex: Int
    ): ArrayBlockingQueue<INetworkMessage> {
        return ArrayBlockingQueue<INetworkMessage>(queueSize)
            .also { it.addAll(eventQueue.filterIndexed { index, _ -> index > fromIndex }) }
    }

    private fun getFirstIndex(
        eventQueue: ArrayBlockingQueue<INetworkMessage>,
        eventClass: Class<out INetworkMessage>
    ): Int? {
        return eventQueue.indexOfFirst { it::class.java == eventClass }.takeIf { it >= 0 }
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
            queueMapper[snifferId]?.removeIf { msg -> msg::class.java == eventClass }
        } finally {
            lock.unlock()
        }
    }

    fun getStoredEventsStr(snifferId: Long): String {
        try {
            lock.lock()
            return getEventQueue(snifferId).joinToString("\n") { it::class.java.simpleName }
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