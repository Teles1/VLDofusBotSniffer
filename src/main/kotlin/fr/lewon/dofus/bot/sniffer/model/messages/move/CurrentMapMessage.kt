package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class CurrentMapMessage : INetworkMessage {

    var mapId = 0.0
    var mapKey = ""

    override fun deserialize(stream: ByteArrayReader) {
        mapId = stream.readDouble()
        mapKey = stream.readUTF()
    }
}