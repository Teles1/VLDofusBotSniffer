package fr.lewon.dofus.bot.sniffer.store

import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.locks.ReentrantLock

object EventStore {

    private const val queueSize = 100
    private val queueMapper = HashMap<Long, HashMap<Long, ArrayBlockingQueue<INetworkMessage>>>()
    private val handlerMapper = HashMap<Class<out INetworkMessage>, ArrayList<EventHandler<INetworkMessage>>>()
    private val lock = ReentrantLock()

    fun addSocketEvent(dofusEvent: INetworkMessage, sequenceNumber: Long, snifferId: Long) {
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
        lock.unlock()
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
        lock.lock()
        val allEvents = getAllEvents(getEventQueue(snifferId))
        lock.unlock()
        return allEvents
    }

    fun <T : INetworkMessage> getAllEvents(eventClass: Class<T>, snifferId: Long): List<T> {
        lock.lock()
        val allEvents = getAllEvents(getEventQueue(snifferId), eventClass)
        lock.unlock()
        return allEvents
    }

    fun containsSequence(
        snifferId: Long,
        mainEventClass: Class<out INetworkMessage>,
        vararg additionalEventClasses: Class<out INetworkMessage>
    ): Boolean {
        return containsSequence(snifferId, mainEventClass, additionalEventClasses.groupingBy { it }.eachCount())
    }

    fun containsSequence(
        snifferId: Long,
        mainEventClass: Class<out INetworkMessage>,
        additionalEventClassesWithCount: Map<Class<out INetworkMessage>, Int>,
    ): Boolean {
        try {
            lock.lock()
            val eventQueue = getEventQueue(snifferId)
            val subQueue = getSubQueue(eventQueue, mainEventClass) ?: return false
            for (entry in additionalEventClassesWithCount) {
                val eventClass = entry.key
                val count = entry.value
                if (getAllEvents(subQueue, eventClass).size < count) {
                    return false
                }
            }
            return true
        } finally {
            lock.unlock()
        }
    }

    private fun getSubQueue(
        eventQueue: HashMap<Long, ArrayBlockingQueue<INetworkMessage>>,
        eventClass: Class<out INetworkMessage>
    ): Map<Long, ArrayBlockingQueue<INetworkMessage>>? {
        for (entry in eventQueue) {
            if (entry.value.firstOrNull { it::class.java == eventClass } != null) {
                return eventQueue.filter { it.key >= entry.key }
            }
        }
        return null
    }

    fun <T : INetworkMessage> getLastEvent(eventClass: Class<T>, snifferId: Long): T? {
        lock.lock()
        val lastEvent = getAllEvents(eventClass, snifferId).lastOrNull()
        lock.unlock()
        return lastEvent
    }

    fun <T : INetworkMessage> getFirstEvent(eventClass: Class<T>, snifferId: Long): T? {
        lock.lock()
        val first = getAllEvents(eventClass, snifferId).firstOrNull()
        lock.unlock()
        return first
    }

    fun clear(snifferId: Long) {
        lock.lock()
        queueMapper[snifferId]?.clear()
        lock.unlock()
    }

    fun <T : INetworkType> clear(eventClass: Class<T>, snifferId: Long) {
        lock.lock()
        queueMapper[snifferId]?.values?.forEach {
            it.removeIf { msg -> msg::class.java == eventClass }
        }
        lock.unlock()
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