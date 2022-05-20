package fr.lewon.dofus.bot.sniffer.model.messages.breeding

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.item.breeding.PaddockItem

class GameDataPaddockObjectAddMessage : INetworkMessage {

    val paddockItemDescription = PaddockItem()

    override fun deserialize(stream: ByteArrayReader) {
        paddockItemDescription.deserialize(stream)
    }
}