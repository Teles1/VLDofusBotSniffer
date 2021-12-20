package fr.lewon.dofus.bot.sniffer.store

import fr.lewon.dofus.bot.sniffer.DofusConnection
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.store.waiters.AbstractMessageWaiter
import fr.lewon.dofus.bot.sniffer.store.waiters.MessageWaiter
import fr.lewon.dofus.bot.sniffer.store.waiters.MultipleMessagesWaiter
import fr.lewon.dofus.bot.sniffer.store.waiters.OrderedMessagesWaiter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.locks.ReentrantLock

class EventStore {

    private val eventQueue = ArrayBlockingQueue<INetworkMessage>(QUEUE_SIZE)
    private val lock = ReentrantLock()
    private val waitLock = ReentrantLock()

    private var messageWaiter: AbstractMessageWaiter? = null


    fun addSocketEvent(dofusEvent: INetworkMessage, connection: DofusConnection) {
        try {
            lock.lockInterruptibly()
            if (!eventQueue.offer(dofusEvent)) {
                eventQueue.poll()
                eventQueue.offer(dofusEvent)
            }
            getMappers(dofusEvent.javaClass).forEach {
                it.onEventReceived(dofusEvent, connection)
            }
            messageWaiter?.takeIf { !it.consumed }?.onMessageReceived(dofusEvent)
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkMessage> getAllEvents(eventClass: Class<T>): List<T> {
        try {
            lock.lockInterruptibly()
            return eventQueue
                .filter { it::class.java == eventClass }
                .map { eventClass.cast(it) }
        } finally {
            lock.unlock()
        }
    }

    fun isAllEventsPresent(vararg messageClasses: Class<out INetworkMessage>): Boolean {
        return isAllEventsPresent(messageClasses.groupingBy { it }.eachCount())
    }

    fun isAllEventsPresent(messageClassByCount: Map<Class<out INetworkMessage>, Int>): Boolean {
        for (entry in messageClassByCount.entries) {
            if (getAllEvents(entry.key).size < entry.value) {
                return false
            }
        }
        return true
    }

    fun waitUntilMessagesArrives(messageClass: Class<out INetworkMessage>, timeout: Int): Boolean {
        return waitUntil(timeout) { MessageWaiter(waitLock, messageClass) }
    }

    fun waitUntilMultipleMessagesArrive(messageClasses: Array<out Class<out INetworkMessage>>, timeout: Int): Boolean {
        return waitUntil(timeout) { MultipleMessagesWaiter(waitLock, messageClasses) }
    }

    fun waitUntilOrderedMessagesArrive(messageClasses: Array<out Class<out INetworkMessage>>, timeout: Int): Boolean {
        return waitUntil(timeout) { OrderedMessagesWaiter(waitLock, messageClasses) }
    }

    private fun waitUntil(timeout: Int, buildWaiter: () -> AbstractMessageWaiter): Boolean {
        try {
            waitLock.lockInterruptibly()
            val newMessageWaiter = buildWaiter()
            messageWaiter = newMessageWaiter
            return newMessageWaiter.waitUntilNotify(timeout.toLong())
        } finally {
            waitLock.unlock()
        }
    }

    fun <T : INetworkMessage> getLastEvent(eventClass: Class<T>): T? {
        try {
            lock.lockInterruptibly()
            return getAllEvents(eventClass).lastOrNull()
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkMessage> getFirstEvent(eventClass: Class<T>): T? {
        try {
            lock.lockInterruptibly()
            return getAllEvents(eventClass).firstOrNull()
        } finally {
            lock.unlock()
        }
    }

    fun clear() {
        try {
            lock.lockInterruptibly()
            eventQueue.clear()
        } finally {
            lock.unlock()
        }
    }

    fun <T : INetworkMessage> clear(eventClass: Class<T>) {
        try {
            lock.lockInterruptibly()
            eventQueue.removeIf { it::class.java == eventClass }
        } finally {
            lock.unlock()
        }
    }

    fun clearUntilFirst(eventClass: Class<out INetworkMessage>) {
        try {
            lock.lockInterruptibly()
            clearUntil(getFirstEvent(eventClass))
        } finally {
            lock.unlock()
        }
    }

    fun clearUntilLast(eventClass: Class<out INetworkMessage>) {
        try {
            lock.lockInterruptibly()
            clearUntil(getLastEvent(eventClass))
        } finally {
            lock.unlock()
        }
    }

    private fun clearUntil(event: INetworkMessage?) {
        try {
            lock.lockInterruptibly()
            while (eventQueue.firstOrNull() != event) {
                eventQueue.poll()
            }
        } finally {
            lock.unlock()
        }
    }

    fun getStoredEventsStr(): String {
        try {
            lock.lockInterruptibly()
            return eventQueue.joinToString("\n") { it::class.java.simpleName }
        } finally {
            lock.unlock()
        }
    }

    companion object {
        private const val QUEUE_SIZE = 500
        private val HANDLER_MAPPER = HashMap<Class<out INetworkMessage>, ArrayList<EventHandler<INetworkMessage>>>()
        private val STATIC_LOCK = ReentrantLock()

        fun <T : INetworkMessage> getMappers(eventClass: Class<T>): ArrayList<EventHandler<T>> {
            try {
                STATIC_LOCK.lockInterruptibly()
                return (HANDLER_MAPPER[eventClass] ?: ArrayList()) as ArrayList<EventHandler<T>>
            } finally {
                STATIC_LOCK.unlock()
            }
        }

        @Synchronized
        fun <T : INetworkMessage> addEventHandler(eventClass: Class<T>, eventHandler: EventHandler<T>) {
            val eventHandlers = HANDLER_MAPPER.computeIfAbsent(eventClass) { ArrayList() }
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

}