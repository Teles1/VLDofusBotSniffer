package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class GameActionFightInvisibilityMessage : AbstractGameActionMessage() {

    var targetId = 0.0
    var state = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        targetId = stream.readDouble()
        state = stream.readUnsignedByte()
    }
}