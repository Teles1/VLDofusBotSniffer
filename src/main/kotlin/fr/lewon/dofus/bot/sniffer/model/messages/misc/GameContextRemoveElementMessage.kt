package fr.lewon.dofus.bot.sniffer.model.messages.misc

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class GameContextRemoveElementMessage : INetworkMessage {

    var id = 0.0

    override fun deserialize(stream: ByteArrayReader) {
        id = stream.readDouble()
    }
}