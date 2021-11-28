package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class SetCharacterRestrictionsMessage : INetworkMessage {
    override fun deserialize(stream: ByteArrayReader) {
    }
}