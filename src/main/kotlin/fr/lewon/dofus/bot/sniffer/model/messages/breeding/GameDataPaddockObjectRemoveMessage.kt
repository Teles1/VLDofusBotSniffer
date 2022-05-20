package fr.lewon.dofus.bot.sniffer.model.messages.breeding

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class GameDataPaddockObjectRemoveMessage : INetworkMessage {

    var cellId = 0

    override fun deserialize(stream: ByteArrayReader) {
        cellId = stream.readVarShort()
    }
}