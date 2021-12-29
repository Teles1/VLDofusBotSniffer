package fr.lewon.dofus.bot.sniffer.model.messages.smithmagic

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.ObjectItem

class ObjectModifiedMessage : INetworkMessage {

    val objectItem = ObjectItem()

    override fun deserialize(stream: ByteArrayReader) {
        objectItem.deserialize(stream)
    }

}