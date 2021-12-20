package fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.monster

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class SpawnMonsterInformation : BaseSpawnMonsterInformation() {

    var creatureGrade = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        creatureGrade = stream.readUnsignedByte()
    }
}