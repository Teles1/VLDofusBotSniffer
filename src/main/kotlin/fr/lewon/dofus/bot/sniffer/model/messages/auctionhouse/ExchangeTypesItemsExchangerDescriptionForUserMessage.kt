package fr.lewon.dofus.bot.sniffer.model.messages.auctionhouse

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.auctionhouse.BidExchangerObjectInfo

class ExchangeTypesItemsExchangerDescriptionForUserMessage : INetworkMessage {

    var objectGID = 0
    var objectType = 0
    val itemTypeDescriptions = ArrayList<BidExchangerObjectInfo>()

    override fun deserialize(stream: ByteArrayReader) {
        objectGID = stream.readVarInt()
        objectType = stream.readInt()
        for (i in 0 until stream.readUnsignedShort()) {
            val itemTypeDescription = BidExchangerObjectInfo()
            itemTypeDescription.deserialize(stream)
            itemTypeDescriptions.add(itemTypeDescription)
        }
    }

}