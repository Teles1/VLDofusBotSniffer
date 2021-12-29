package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

open class ExchangeCraftResultMessage : INetworkMessage {

    var craftResult = 0

    override fun deserialize(stream: ByteArrayReader) {
        craftResult = stream.readUnsignedByte()
    }
}