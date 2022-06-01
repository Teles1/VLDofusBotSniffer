package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class ExchangeObjectRemovedMessage : ExchangeObjectMessage() {

    var objectUID = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        objectUID = stream.readVarInt()
    }
}