package fr.lewon.dofus.bot.sniffer.model.messages.treasurehunt

import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class TreasureHuntFinishedMessage : INetworkMessage {
    override fun deserialize(stream: ByteArrayReader) {}
}