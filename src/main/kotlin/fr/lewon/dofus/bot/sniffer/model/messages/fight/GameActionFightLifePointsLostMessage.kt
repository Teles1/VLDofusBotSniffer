package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class GameActionFightLifePointsLostMessage : AbstractGameActionMessage() {

    var targetId = 0.0
    var loss = 0
    var permanentDamages = 0
    var elementId = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        targetId = stream.readDouble()
        loss = stream.readVarInt()
        permanentDamages = stream.readVarInt()
        elementId = stream.readVarInt()
    }
}