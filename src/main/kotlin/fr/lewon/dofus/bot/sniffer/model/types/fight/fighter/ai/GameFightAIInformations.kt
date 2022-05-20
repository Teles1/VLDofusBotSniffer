package fr.lewon.dofus.bot.sniffer.model.types.fight.fighter.ai

import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook
import fr.lewon.dofus.bot.sniffer.model.types.fight.GameContextBasicSpawnInformation
import fr.lewon.dofus.bot.sniffer.model.types.fight.charac.GameFightCharacteristics
import fr.lewon.dofus.bot.sniffer.model.types.fight.disposition.EntityDispositionInformations
import fr.lewon.dofus.bot.sniffer.model.types.fight.fighter.GameFightFighterInformations

open class GameFightAIInformations : GameFightFighterInformations() {
    
    fun initGameFightAIInformations(
        contextualId: Double,
        disposition: EntityDispositionInformations,
        look: EntityLook,
        spawnInfo: GameContextBasicSpawnInformation,
        wave: Int,
        stats: GameFightCharacteristics,
        previousPositions: ArrayList<Int>
    ) {
        initGameFightFighterInformations(contextualId, disposition, look, spawnInfo, wave, stats, previousPositions)
    }

}