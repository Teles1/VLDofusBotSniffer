package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class GameFightStartingMessage : INetworkMessage {

    var fightType = 0
    var fightId = 0
    var attackerId = 0.0
    var defenderId = 0.0
    var containsBoss = false

    override fun deserialize(stream: ByteArrayReader) {
        fightType = stream.readUnsignedByte()
        fightId = stream.readVarShort()
        attackerId = stream.readDouble()
        defenderId = stream.readDouble()
        containsBoss = stream.readBoolean()
    }
}