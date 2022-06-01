package fr.lewon.dofus.bot.sniffer.model.messages.storage

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.ObjectItem

open class InventoryContentMessage : INetworkMessage {

    val objectItems = ArrayList<ObjectItem>()
    var kamas = 0L

    override fun deserialize(stream: ByteArrayReader) {
        for (i in 0 until stream.readUnsignedShort()) {
            val objectItem = ObjectItem()
            objectItem.deserialize(stream)
            objectItems.add(objectItem)
        }
        kamas = stream.readVarLong()
    }
}