package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.ObjectItemToSellInHumanVendorShop

class ExchangeStartOkHumanVendorMessage : INetworkMessage {

    var sellerId = 0.0
    val objectInfoList = ArrayList<ObjectItemToSellInHumanVendorShop>()

    override fun deserialize(stream: ByteArrayReader) {
        sellerId = stream.readDouble()
        for (i in 0 until stream.readUnsignedShort()) {
            val objectInfo = ObjectItemToSellInHumanVendorShop()
            objectInfo.deserialize(stream)
            objectInfoList.add(objectInfo)
        }
    }
}