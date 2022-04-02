package fr.lewon.dofus.bot.sniffer.model.messages.arena

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class GameRolePlayArenaSwitchToGameServerMessage : INetworkMessage {

    var validToken = false
    val ticket = ArrayList<Int>()
    var homeServerId = 0

    override fun deserialize(stream: ByteArrayReader) {
        validToken = stream.readBoolean()
        for (i in 0 until stream.readVarInt()) {
            ticket.add(stream.readUnsignedByte())
        }
        homeServerId = stream.readUnsignedShort()
    }

}