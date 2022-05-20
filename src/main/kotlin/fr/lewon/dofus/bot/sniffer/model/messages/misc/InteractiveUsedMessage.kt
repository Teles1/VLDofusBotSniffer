package fr.lewon.dofus.bot.sniffer.model.messages.misc

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class InteractiveUsedMessage : INetworkMessage {

    var entityId = 0L
    var elemId = 0
    var skillId = 0
    var duration = 0
    var canMove = false

    override fun deserialize(stream: ByteArrayReader) {
        entityId = stream.readVarLong()
        elemId = stream.readVarInt()
        skillId = stream.readVarShort()
        duration = stream.readVarShort()
        canMove = stream.readBoolean()
    }
}