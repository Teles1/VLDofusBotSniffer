package fr.lewon.dofus.bot.sniffer.store.waiters

import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

abstract class AbstractMessageWaiter(val lock: ReentrantLock) {

    private val condition = lock.newCondition()
    var consumed = false
        private set

    protected fun notifyWaitingThread() {
        if (!consumed) {
            try {
                lock.lockInterruptibly()
                condition.signal()
                consumed = true
            } finally {
                lock.unlock()
            }
        }
    }

    abstract fun onMessageReceived(message: INetworkMessage)

    fun waitUntilNotify(timeoutMillis: Long): Boolean {
        return condition.await(timeoutMillis, TimeUnit.MILLISECONDS)
    }

}