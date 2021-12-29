package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class ExchangeCraftResultMagicWithObjectDescMessage : ExchangeCraftResultWithObjectDescMessage() {

    var magicPoolStatus = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        magicPoolStatus = stream.readUnsignedByte()
    }
}