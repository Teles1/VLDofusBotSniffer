package fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.merchant

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class SellerBuyerDescriptor : INetworkType {

    val quantities = ArrayList<Int>()
    val types = ArrayList<Int>()
    var taxPercentage = 0f
    var taxModificationPercentage = 0f
    var maxItemLevel = 0
    var maxItemPerAccount = 0
    var npcContextualId = 0
    var unsoldDelay = 0

    override fun deserialize(stream: ByteArrayReader) {
        for (i in 0 until stream.readUnsignedShort()) {
            quantities.add(stream.readVarInt())
        }
        for (i in 0 until stream.readUnsignedShort()) {
            types.add(stream.readVarInt())
        }
        taxPercentage = stream.readFloat()
        taxModificationPercentage = stream.readFloat()
        maxItemLevel = stream.readUnsignedByte()
        maxItemPerAccount = stream.readVarInt()
        npcContextualId = stream.readInt()
        unsoldDelay = stream.readVarShort()
    }
}