package fr.lewon.dofus.bot.sniffer.model.types.game.data.items

import fr.lewon.dofus.bot.sniffer.model.types.game.data.items.effects.ObjectEffect
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ObjectItemToSellInHumanVendorShop : Item() {
	var objectGID: Int = 0
	var effects: ArrayList<ObjectEffect> = ArrayList()
	var objectUID: Int = 0
	var quantity: Int = 0
	var objectPrice: Double = 0.0
	var publicPrice: Double = 0.0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		objectGID = stream.readVarInt().toInt()
		effects = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = ProtocolTypeManager.getInstance<ObjectEffect>(stream.readUnsignedShort())
			item.deserialize(stream)
			effects.add(item)
		}
		objectUID = stream.readVarInt().toInt()
		quantity = stream.readVarInt().toInt()
		objectPrice = stream.readVarLong().toDouble()
		publicPrice = stream.readVarLong().toDouble()
	}
}
