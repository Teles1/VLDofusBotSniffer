package fr.lewon.dofus.bot.sniffer.model.types.fight.fighter

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook
import fr.lewon.dofus.bot.sniffer.model.types.fight.GameContextBasicSpawnInformation
import fr.lewon.dofus.bot.sniffer.model.types.fight.charac.GameFightCharacteristics
import fr.lewon.dofus.bot.sniffer.model.types.fight.disposition.EntityDispositionInformations

class GameFightEntityInformation : GameFightFighterInformations() {

    var entityModelId = 0
    var level = 0
    var masterId = 0.0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        entityModelId = stream.readByte().toInt()
        level = stream.readVarShort()
        masterId = stream.readDouble()
    }

    fun initGameFightEntityInformation(
        contextualId: Double,
        disposition: EntityDispositionInformations,
        look: EntityLook,
        spawnInfo: GameContextBasicSpawnInformation,
        wave: Int,
        stats: GameFightCharacteristics,
        previousPositions: ArrayList<Int>,
        entityModelId: Int,
        level: Int,
        masterId: Double
    ) {
        initGameFightFighterInformations(contextualId, disposition, look, spawnInfo, wave, stats, previousPositions)
        this.entityModelId = entityModelId
        this.level = level
        this.masterId = masterId
    }
}