package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.ObjectItem

class ExchangeObjectAddedMessage : ExchangeObjectMessage() {

    var objectItem = ObjectItem()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        objectItem.deserialize(stream)
    }
}