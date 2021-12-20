package fr.lewon.dofus.bot.sniffer.store.waiters

import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import java.util.concurrent.locks.ReentrantLock

class MessageWaiter(lock: ReentrantLock, private val messageClass: Class<out INetworkMessage>) :
    AbstractMessageWaiter(lock) {

    override fun onMessageReceived(message: INetworkMessage) {
        if (messageClass == message::class.java) {
            notifyWaitingThread()
        }
    }

}