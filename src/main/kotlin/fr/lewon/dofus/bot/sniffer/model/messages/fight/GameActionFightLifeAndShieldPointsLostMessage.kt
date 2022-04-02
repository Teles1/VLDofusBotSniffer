package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class GameActionFightLifeAndShieldPointsLostMessage : GameActionFightLifePointsLostMessage() {

    var shieldLoss = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        shieldLoss = stream.readVarShort()
    }
}