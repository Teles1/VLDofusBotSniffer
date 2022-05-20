package fr.lewon.dofus.bot.sniffer.model.types.fight.fighter

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.types.actor.GameContextActorInformations
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook
import fr.lewon.dofus.bot.sniffer.model.types.fight.GameContextBasicSpawnInformation
import fr.lewon.dofus.bot.sniffer.model.types.fight.charac.GameFightCharacteristics
import fr.lewon.dofus.bot.sniffer.model.types.fight.disposition.EntityDispositionInformations

open class GameFightFighterInformations : GameContextActorInformations() {

    var spawnInfo = GameContextBasicSpawnInformation()
    var wave = 0
    lateinit var stats: GameFightCharacteristics
    var previousPositions = ArrayList<Int>()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        spawnInfo.deserialize(stream)
        wave = stream.readByte().toInt()
        stats = TypeManager.getInstance(stream.readUnsignedShort())
        stats.deserialize(stream)
        for (i in 0 until stream.readUnsignedShort()) {
            val previousPos = stream.readVarShort()
            previousPositions.add(previousPos)
        }

    }

    fun initGameFightFighterInformations(
        contextualId: Double,
        disposition: EntityDispositionInformations,
        look: EntityLook,
        spawnInfo: GameContextBasicSpawnInformation,
        wave: Int,
        stats: GameFightCharacteristics,
        previousPositions: ArrayList<Int>
    ) {
        initGameContextActorInformations(contextualId, disposition, look)
        this.spawnInfo = spawnInfo
        this.wave = wave
        this.stats = stats
        this.previousPositions = previousPositions
    }

}