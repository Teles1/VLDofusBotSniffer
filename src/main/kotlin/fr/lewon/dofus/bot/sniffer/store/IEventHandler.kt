package fr.lewon.dofus.bot.sniffer.store

import fr.lewon.dofus.bot.sniffer.DofusConnection
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

interface IEventHandler<T : INetworkMessage> {

    fun onEventReceived(socketResult: T, connection: DofusConnection)

}