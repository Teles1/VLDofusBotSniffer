package fr.lewon.dofus.bot.sniffer.model.messages.breeding

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.item.breeding.PaddockItem

class GameDataPaddockObjectListAddMessage : INetworkMessage {

    val paddockItemsDescription = ArrayList<PaddockItem>()

    override fun deserialize(stream: ByteArrayReader) {
        for (i in 0 until stream.readUnsignedShort()) {
            val paddockItem = PaddockItem()
            paddockItem.deserialize(stream)
            paddockItemsDescription.add(paddockItem)
        }
    }
}