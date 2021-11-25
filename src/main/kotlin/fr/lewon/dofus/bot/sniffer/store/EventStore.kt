package fr.lewon.dofus.bot.sniffer.store

import fr.lewon.dofus.bot.sniffer.model.INetworkType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.concurrent.ArrayBlockingQueue

object EventStore {

    private const val queueSize = 100
    private val queueMapper = HashMap<Long, HashMap<Class<out INetworkType>, ArrayBlockingQueue<INetworkType>>>()
    private val handlerMapper = HashMap<Class<out INetworkType>, ArrayList<EventHandler<INetworkType>>>()

    fun addSocketEvent(dofusEvent: INetworkType, snifferId: Long) {
        val eventQueue: ArrayBlockingQueue<INetworkType>
        synchronized(queueMapper) {
            eventQueue = queueMapper.computeIfAbsent(snifferId) { HashMap() }
                .computeIfAbsent(dofusEvent.javaClass) { ArrayBlockingQueue(queueSize) }
        }
        synchronized(eventQueue) {
            if (!eventQueue.offer(dofusEvent)) {
                eventQueue.poll()
                eventQueue.offer(dofusEvent)
            }
        }
        synchronized(handlerMapper) {
            handlerMapper[dofusEvent.javaClass]?.forEach {
                it.onEventReceived(dofusEvent, snifferId)
            }
        }
    }

    fun <T : INetworkType> getAllEvents(eventClass: Class<T>, snifferId: Long): List<T> {
        val eventQueue: ArrayBlockingQueue<INetworkType>
        synchronized(queueMapper) {
            eventQueue = queueMapper.computeIfAbsent(snifferId) { HashMap() }
                .computeIfAbsent(eventClass) { ArrayBlockingQueue(queueSize) }
        }
        return eventQueue.map { eventClass.cast(it) }
    }

    fun <T : INetworkType> getLastEvent(eventClass: Class<T>, snifferId: Long): T? {
        val eventQueue: ArrayBlockingQueue<INetworkType>
        synchronized(queueMapper) {
            eventQueue = queueMapper.computeIfAbsent(snifferId) { HashMap() }
                .computeIfAbsent(eventClass) { ArrayBlockingQueue(queueSize) }
        }
        return eventClass.cast(eventQueue.lastOrNull())
    }

    fun clear(snifferId: Long) {
        synchronized(queueMapper) { queueMapper[snifferId]?.clear() }
    }

    fun <T : INetworkType> clear(eventClass: Class<T>, snifferId: Long) {
        synchronized(queueMapper) {
            queueMapper[snifferId]?.get(eventClass)?.clear()
        }
    }

    @Synchronized
    fun <T : INetworkType> addEventHandler(eventClass: Class<T>, eventHandler: EventHandler<T>) {
        synchronized(handlerMapper) {
            val eventHandlers = handlerMapper.computeIfAbsent(eventClass) { ArrayList() }
            synchronized(eventHandlers) {
                eventHandlers.add(eventHandler as EventHandler<INetworkType>)
            }
        }
    }

    fun <T : INetworkType> addEventHandler(eventHandler: EventHandler<T>) {
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