package fr.lewon.dofus.bot.sniffer.model.messages.stats

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class InventoryWeightMessage : INetworkMessage {

    var inventoryWeight = 0
    var shopWeight = 0
    var weightMax = 0

    override fun deserialize(stream: ByteArrayReader) {
        inventoryWeight = stream.readVarInt()
        shopWeight = stream.readVarInt()
        weightMax = stream.readVarInt()
    }
}