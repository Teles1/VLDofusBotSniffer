package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class GameActionFightLifePointsGainMessage : AbstractGameActionMessage() {

    var targetId = 0.0
    var delta = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        targetId = stream.readDouble()
        delta = stream.readVarInt()
    }
}