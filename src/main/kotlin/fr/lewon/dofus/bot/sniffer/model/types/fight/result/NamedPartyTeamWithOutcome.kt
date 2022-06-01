package fr.lewon.dofus.bot.sniffer.model.types.fight.result

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class NamedPartyTeamWithOutcome : INetworkType {

    val team = NamedPartyTeam()
    var outcome = 0

    override fun deserialize(stream: ByteArrayReader) {
        team.deserialize(stream)
        outcome = stream.readVarShort()
    }
}