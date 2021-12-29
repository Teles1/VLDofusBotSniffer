package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.ObjectItemNotInContainer

open class ExchangeCraftResultWithObjectDescMessage : ExchangeCraftResultMessage() {

    val objectInfo = ObjectItemNotInContainer()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        objectInfo.deserialize(stream)
    }
}