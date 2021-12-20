package fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.monster

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.SpawnInformation

open class BaseSpawnMonsterInformation : SpawnInformation() {

    var creatureGenericId = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        creatureGenericId = stream.readVarShort()
    }
}