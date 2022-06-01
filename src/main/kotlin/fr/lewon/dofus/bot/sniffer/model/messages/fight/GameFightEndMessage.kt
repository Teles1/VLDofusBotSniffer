package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.fight.result.FightResultListEntry
import fr.lewon.dofus.bot.sniffer.model.types.fight.result.NamedPartyTeamWithOutcome

class GameFightEndMessage : INetworkMessage {

    var duration = 0
    var rewardRate = 0
    var lootShareLimitMalus = 0
    val results = ArrayList<FightResultListEntry>()
    val namedPartyTeamsOutcomes = ArrayList<NamedPartyTeamWithOutcome>()

    override fun deserialize(stream: ByteArrayReader) {
        duration = stream.readInt()
        rewardRate = stream.readVarShort()
        lootShareLimitMalus = stream.readUnsignedShort()
        for (i in 0 until stream.readUnsignedShort()) {
            val result: FightResultListEntry = TypeManager.getInstance(stream.readUnsignedShort())
            result.deserialize(stream)
            results.add(result)
        }
        for (i in 0 until stream.readUnsignedShort()) {
            val namedPartyTeamWithOutcome = NamedPartyTeamWithOutcome()
            namedPartyTeamWithOutcome.deserialize(stream)
            namedPartyTeamsOutcomes.add(namedPartyTeamWithOutcome)
        }
    }
}