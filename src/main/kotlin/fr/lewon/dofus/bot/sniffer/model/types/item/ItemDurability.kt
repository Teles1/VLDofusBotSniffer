package fr.lewon.dofus.bot.sniffer.model.types.item

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class ItemDurability : INetworkType {

    var durability = 0
    var durabilityMax = 0

    override fun deserialize(stream: ByteArrayReader) {
        durability = stream.readUnsignedShort()
        durabilityMax = stream.readUnsignedShort()
    }
}