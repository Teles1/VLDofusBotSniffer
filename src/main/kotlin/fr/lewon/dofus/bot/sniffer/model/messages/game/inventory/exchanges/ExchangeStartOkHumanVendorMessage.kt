package fr.lewon.dofus.bot.sniffer.model.messages.game.inventory.exchanges

import fr.lewon.dofus.bot.sniffer.model.types.game.data.items.ObjectItemToSellInHumanVendorShop
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ExchangeStartOkHumanVendorMessage : NetworkMessage() {
	var sellerId: Double = 0.0
	var objectsInfos: ArrayList<ObjectItemToSellInHumanVendorShop> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		sellerId = stream.readDouble().toDouble()
		objectsInfos = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = ObjectItemToSellInHumanVendorShop()
			item.deserialize(stream)
			objectsInfos.add(item)
		}
	}
}
