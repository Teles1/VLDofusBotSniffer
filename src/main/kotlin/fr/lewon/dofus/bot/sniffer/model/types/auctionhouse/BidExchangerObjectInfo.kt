package fr.lewon.dofus.bot.sniffer.model.types.auctionhouse

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.effect.ObjectEffect

class BidExchangerObjectInfo : INetworkType {

    var objectUID = 0
    var objectGID = 0
    var objectType = 0
    val effects = ArrayList<ObjectEffect>()
    val prices = ArrayList<Long>()

    override fun deserialize(stream: ByteArrayReader) {
        objectUID = stream.readVarInt()
        objectGID = stream.readVarShort()
        objectType = stream.readInt()
        for (i in 0 until stream.readUnsignedShort()) {
            val effect = TypeManager.getInstance<ObjectEffect>(stream.readUnsignedShort())
            effect.deserialize(stream)
            effects.add(effect)
        }
        for (i in 0 until stream.readUnsignedShort()) {
            prices.add(stream.readVarLong())
        }
    }

}