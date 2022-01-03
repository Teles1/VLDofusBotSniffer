package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class GameFightOptionStateUpdateMessage : INetworkMessage {

    var fightId = 0
    var teamId = 0
    var option = 0
    var state = false

    override fun deserialize(stream: ByteArrayReader) {
        fightId = stream.readVarShort()
        teamId = stream.readUnsignedByte()
        option = stream.readUnsignedByte()
        state = stream.readBoolean()
    }
}