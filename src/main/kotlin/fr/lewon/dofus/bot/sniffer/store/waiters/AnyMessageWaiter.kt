package fr.lewon.dofus.bot.sniffer.store.waiters

import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import java.util.concurrent.locks.ReentrantLock

class AnyMessageWaiter(lock: ReentrantLock, messageClasses: Array<out Class<out INetworkMessage>>) :
    AbstractMessageWaiter(lock) {

    private val remainingMessageClasses = messageClasses.toMutableList()

    override fun onMessageReceived(message: INetworkMessage) {
        if (remainingMessageClasses.contains(message::class.java)) {
            notifyWaitingThread()
        }
    }

}