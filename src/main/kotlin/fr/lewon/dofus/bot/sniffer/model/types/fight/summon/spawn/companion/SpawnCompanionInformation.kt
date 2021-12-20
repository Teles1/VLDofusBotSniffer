package fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.companion

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.SpawnInformation

class SpawnCompanionInformation : SpawnInformation() {

    var modelId = 0
    var level = 0
    var summonerId = 0.0
    var ownerId = 0.0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        modelId = stream.readUnsignedByte()
        level = stream.readVarShort()
        summonerId = stream.readDouble()
        ownerId = stream.readDouble()
    }
}