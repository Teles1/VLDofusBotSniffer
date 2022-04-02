package fr.lewon.dofus.bot.sniffer.model.messages.arena

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class GameRolePlayArenaFightPropositionMessage : INetworkMessage {

    var fightId = 0
    val alliesId = ArrayList<Double>()
    var duration = 0

    override fun deserialize(stream: ByteArrayReader) {
        fightId = stream.readVarShort()
        for (i in 0 until stream.readUnsignedShort()) {
            alliesId.add(stream.readDouble())
        }
        duration = stream.readVarShort()
    }

}