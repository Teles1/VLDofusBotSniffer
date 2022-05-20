package fr.lewon.dofus.bot.sniffer.model.types.fight.fighter.ai

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook
import fr.lewon.dofus.bot.sniffer.model.types.fight.GameContextBasicSpawnInformation
import fr.lewon.dofus.bot.sniffer.model.types.fight.charac.GameFightCharacteristics
import fr.lewon.dofus.bot.sniffer.model.types.fight.disposition.EntityDispositionInformations

open class GameFightMonsterInformations : GameFightAIInformations() {

    var creatureGenericId = 0
    var creatureGrade = 0
    var creatureLevel = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        creatureGenericId = stream.readVarShort()
        creatureGrade = stream.readByte().toInt()
        creatureLevel = stream.readUnsignedShort()
    }

    fun initGameFightMonsterInformations(
        contextualId: Double,
        disposition: EntityDispositionInformations,
        look: EntityLook,
        spawnInfo: GameContextBasicSpawnInformation,
        wave: Int,
        stats: GameFightCharacteristics,
        previousPositions: ArrayList<Int>,
        creatureGenericId: Int,
        creatureGrade: Int,
        creatureLevel: Int
    ) {
        initGameFightAIInformations(contextualId, disposition, look, spawnInfo, wave, stats, previousPositions)
        this.creatureGenericId = creatureGenericId
        this.creatureGrade = creatureGrade
        this.creatureLevel = creatureLevel
    }
}