package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.fight.charac.CharacterCharacteristicsInformations

class CharacterStatsListMessage : INetworkMessage {

    var stats = CharacterCharacteristicsInformations()

    override fun deserialize(stream: ByteArrayReader) {
        stats.deserialize(stream)
    }
}