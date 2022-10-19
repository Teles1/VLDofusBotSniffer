package fr.lewon.dofus.bot.sniffer.model.messages.game.inventory.exchanges

import fr.lewon.dofus.bot.sniffer.model.types.game.data.items.ObjectItemToSell
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ExchangeShopStockMultiMovementUpdatedMessage : NetworkMessage() {
	var objectInfoList: ArrayList<ObjectItemToSell> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		objectInfoList = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = ObjectItemToSell()
			item.deserialize(stream)
			objectInfoList.add(item)
		}
	}
}
