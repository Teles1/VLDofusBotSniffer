package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

open class ExchangeStartedMessage : INetworkMessage {

    var exchangeType = 0

    override fun deserialize(stream: ByteArrayReader) {
        exchangeType = stream.readUnsignedByte()
    }
}