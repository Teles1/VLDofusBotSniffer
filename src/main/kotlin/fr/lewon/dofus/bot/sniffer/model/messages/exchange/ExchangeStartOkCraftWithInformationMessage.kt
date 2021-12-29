package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class ExchangeStartOkCraftWithInformationMessage : ExchangeStartOkCraftMessage() {

    var skillId = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        skillId = stream.readVarInt()
    }
}