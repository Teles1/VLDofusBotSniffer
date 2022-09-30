package fr.lewon.dofus.bot.sniffer.model.messages.misc

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class BasicAckMessage : INetworkMessage {

    var seq = 0
    var lastPacketId = 0

    override fun deserialize(stream: ByteArrayReader) {
        seq = stream.readVarInt()
        lastPacketId = stream.readVarShort()
    }
}