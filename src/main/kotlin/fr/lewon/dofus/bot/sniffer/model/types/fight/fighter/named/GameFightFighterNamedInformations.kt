package fr.lewon.dofus.bot.sniffer.model.types.fight.fighter.named

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook
import fr.lewon.dofus.bot.sniffer.model.types.fight.GameContextBasicSpawnInformation
import fr.lewon.dofus.bot.sniffer.model.types.fight.charac.GameFightCharacteristics
import fr.lewon.dofus.bot.sniffer.model.types.fight.disposition.EntityDispositionInformations
import fr.lewon.dofus.bot.sniffer.model.types.fight.fighter.GameFightFighterInformations
import fr.lewon.dofus.bot.sniffer.model.types.fight.fighter.PlayerStatus

open class GameFightFighterNamedInformations : GameFightFighterInformations() {

    var name = ""
    var status = PlayerStatus()
    var leagueId = 0
    var ladderPosition = 0
    var hiddenInPrefight = false

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        name = stream.readUTF()
        status.deserialize(stream)
        leagueId = stream.readVarShort()
        ladderPosition = stream.readInt()
        hiddenInPrefight = stream.readBoolean()
    }

    fun initGameFightFighterNamedInformations(
        contextualId: Double,
        disposition: EntityDispositionInformations,
        look: EntityLook,
        spawnInfo: GameContextBasicSpawnInformation,
        wave: Int,
        stats: GameFightCharacteristics,
        previousPositions: ArrayList<Int>,
        name: String
    ) {
        initGameFightFighterInformations(contextualId, disposition, look, spawnInfo, wave, stats, previousPositions)
        this.name = name
    }
}