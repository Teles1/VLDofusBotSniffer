package fr.lewon.dofus.bot.sniffer.model.messages.game.inventory.exchanges

import fr.lewon.dofus.bot.sniffer.model.types.game.data.items.ObjectItemGenericQuantity
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ExchangeGuildTaxCollectorGetMessage : NetworkMessage() {
	var collectorName: String = ""
	var worldX: Int = 0
	var worldY: Int = 0
	var mapId: Double = 0.0
	var subAreaId: Int = 0
	var userName: String = ""
	var callerId: Double = 0.0
	var callerName: String = ""
	var experience: Double = 0.0
	var pods: Int = 0
	var objectsInfos: ArrayList<ObjectItemGenericQuantity> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		collectorName = stream.readUTF()
		worldX = stream.readUnsignedShort().toInt()
		worldY = stream.readUnsignedShort().toInt()
		mapId = stream.readDouble().toDouble()
		subAreaId = stream.readVarShort().toInt()
		userName = stream.readUTF()
		callerId = stream.readVarLong().toDouble()
		callerName = stream.readUTF()
		experience = stream.readDouble().toDouble()
		pods = stream.readVarShort().toInt()
		objectsInfos = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = ObjectItemGenericQuantity()
			item.deserialize(stream)
			objectsInfos.add(item)
		}
	}
	override fun getNetworkMessageId(): Int = 4113
}
