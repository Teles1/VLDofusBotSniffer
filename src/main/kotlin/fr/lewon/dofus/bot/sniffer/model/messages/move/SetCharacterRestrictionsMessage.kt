package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class SetCharacterRestrictionsMessage : INetworkMessage {

    var actorId = 0.0

    override fun deserialize(stream: ByteArrayReader) {
        actorId = stream.readDouble()
    }
}