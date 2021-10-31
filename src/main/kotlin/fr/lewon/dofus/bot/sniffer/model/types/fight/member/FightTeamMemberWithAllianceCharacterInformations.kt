package fr.lewon.dofus.bot.sniffer.model.types.fight.member

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.human.social.alliance.BasicAllianceInformations

class FightTeamMemberWithAllianceCharacterInformations : FightTeamMemberCharacterInformations() {

    lateinit var allianceInfos: BasicAllianceInformations

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        allianceInfos = BasicAllianceInformations()
        allianceInfos.deserialize(stream)
    }
}