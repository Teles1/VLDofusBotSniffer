package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.merchant.SellerBuyerDescriptor

class ExchangeStartedBidBuyerMessage : INetworkMessage {

    val buyerDescriptor = SellerBuyerDescriptor()

    override fun deserialize(stream: ByteArrayReader) {
        buyerDescriptor.deserialize(stream)
    }
}