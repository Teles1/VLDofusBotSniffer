package fr.lewon.dofus.bot.sniffer.model.types.fight.result

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class NamedPartyTeam : INetworkType {

    var teamId = 0
    var partyName = ""

    override fun deserialize(stream: ByteArrayReader) {
        teamId = stream.readUnsignedByte()
        partyName = stream.readUTF()
    }
}