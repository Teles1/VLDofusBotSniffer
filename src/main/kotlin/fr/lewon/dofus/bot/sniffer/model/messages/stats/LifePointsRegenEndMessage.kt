package fr.lewon.dofus.bot.sniffer.model.messages.stats

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class LifePointsRegenEndMessage : UpdateLifePointsMessage() {

    var lifePointsGained = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        lifePointsGained = stream.readVarInt()
    }
}