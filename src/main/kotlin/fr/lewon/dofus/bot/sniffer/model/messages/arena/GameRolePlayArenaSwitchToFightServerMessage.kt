package fr.lewon.dofus.bot.sniffer.model.messages.arena

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class GameRolePlayArenaSwitchToFightServerMessage : INetworkMessage {

    var address = ""
    val ports = ArrayList<Int>()
    val ticket = ArrayList<Int>()

    override fun deserialize(stream: ByteArrayReader) {
        address = stream.readUTF()
        for (i in 0 until stream.readUnsignedShort()) {
            ports.add(stream.readVarShort())
        }
        for (i in 0 until stream.readVarInt()) {
            ticket.add(stream.readUnsignedByte())
        }
    }

}