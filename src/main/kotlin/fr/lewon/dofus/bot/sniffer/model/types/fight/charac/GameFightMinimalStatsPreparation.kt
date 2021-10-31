package fr.lewon.dofus.bot.sniffer.model.types.fight.charac

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class GameFightMinimalStatsPreparation : GameFightMinimalStats() {

    var initiative = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        initiative = stream.readVarInt()
    }
}