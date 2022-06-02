package fr.lewon.dofus.bot.sniffer.model.messages.misc

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.ObjectItem

class ObjectAddedMessage : INetworkMessage {

    val objectItem = ObjectItem()
    var origin = 0

    override fun deserialize(stream: ByteArrayReader) {
        objectItem.deserialize(stream)
        origin = stream.readUnsignedByte()
    }
}