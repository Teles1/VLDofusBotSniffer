package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class ExchangeBuyOkMessage : INetworkMessage {
    override fun deserialize(stream: ByteArrayReader) {
    }
}