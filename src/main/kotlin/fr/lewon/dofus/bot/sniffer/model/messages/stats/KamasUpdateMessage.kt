package fr.lewon.dofus.bot.sniffer.model.messages.stats

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class KamasUpdateMessage : INetworkMessage {

    var kamasTotal = 0L

    override fun deserialize(stream: ByteArrayReader) {
        kamasTotal = stream.readVarLong()
    }
}