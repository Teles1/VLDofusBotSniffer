package fr.lewon.dofus.bot.sniffer.model.messages.stats

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

open class UpdateLifePointsMessage : INetworkMessage {

    var lifePoints = 0
    var maxLifePoints = 0

    override fun deserialize(stream: ByteArrayReader) {
        lifePoints = stream.readVarInt()
        maxLifePoints = stream.readVarInt()
    }
}