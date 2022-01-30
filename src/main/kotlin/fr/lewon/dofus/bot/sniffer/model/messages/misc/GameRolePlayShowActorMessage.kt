package fr.lewon.dofus.bot.sniffer.model.messages.misc

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.GameRolePlayActorInformations

class GameRolePlayShowActorMessage : INetworkMessage {

    lateinit var information: GameRolePlayActorInformations

    override fun deserialize(stream: ByteArrayReader) {
        information = TypeManager.getInstance(stream.readUnsignedShort())
        information.deserialize(stream)
    }
}