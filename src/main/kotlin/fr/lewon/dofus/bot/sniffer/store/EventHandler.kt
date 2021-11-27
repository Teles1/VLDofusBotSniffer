package fr.lewon.dofus.bot.sniffer.store

import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

interface EventHandler<T : INetworkMessage> {

    fun onEventReceived(socketResult: T, snifferId: Long)

}