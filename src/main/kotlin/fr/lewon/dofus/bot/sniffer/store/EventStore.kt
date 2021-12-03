package fr.lewon.dofus.bot.sniffer.store

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

    fun <T : INetworkMessage> getAllEvents(eventClass: Class<T>, snifferId: Long): List<T> {
        try {
            lock.lock()
            return getAllEvents(getEventQueue(snifferId), eventClass)
        } finally {
            lock.unlock()
        }
    }

    fun isAllEventsPresent(snifferId: Long, vararg messageClasses: Class<out INetworkMessage>): Boolean {
        return isAllEventsPresent(snifferId, messageClasses.groupingBy { it }.eachCount())
    }

    fun isAllEventsPresent(snifferId: Long, messageClassByCount: Map<Class<out INetworkMessage>, Int>): Boolean {
        for (entry in messageClassByCount.entries) {
            if (getAllEvents(entry.key, snifferId).size < entry.value) {
                return false
            }
        }
        return true
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
            getEventQueue(snifferId).clear()
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkMessage> clear(eventClass: Class<T>, snifferId: Long) {
        try {
            lock.lock()
            getEventQueue(snifferId).removeIf { it::class.java == eventClass }
        } finally {
            lock.unlock()
        }
    }

    fun clearUntilFirst(snifferId: Long, eventClass: Class<out INetworkMessage>) {
        try {
            lock.lock()
            clearUntil(snifferId, getFirstEvent(eventClass, snifferId))
        } finally {
            lock.unlock()
        }
    }

    fun clearUntilLast(snifferId: Long, eventClass: Class<out INetworkMessage>) {
        try {
            lock.lock()
            clearUntil(snifferId, getLastEvent(eventClass, snifferId))
        } finally {
            lock.unlock()
        }
    }

    private fun clearUntil(snifferId: Long, event: INetworkMessage?) {
        try {
            lock.lock()
            val eventQueue = getEventQueue(snifferId)
            while (eventQueue.firstOrNull() != event) {
                eventQueue.poll()
            }
        } finally {
            lock.unlock()
        }
    }

    fun removeEvents(messages: List<INetworkMessage>, snifferId: Long) {
        try {
            lock.lock()
            getEventQueue(snifferId).removeAll(messages.toSet())
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkMessage> removeEvent(eventClass: Class<T>, snifferId: Long, amountToRemove: Int) {
        try {
            lock.lock()
            val eventQueue = getEventQueue(snifferId)
            for (i in 0 until amountToRemove) {
                val toRemove = getFirstEvent(eventClass, snifferId)
                    ?: error("Event not found in queue : ${eventClass.simpleName}")
                eventQueue.remove(toRemove)
            }
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