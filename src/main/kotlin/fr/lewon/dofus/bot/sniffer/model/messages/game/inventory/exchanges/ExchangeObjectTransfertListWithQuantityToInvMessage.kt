package fr.lewon.dofus.bot.sniffer.model.messages.game.inventory.exchanges

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ExchangeObjectTransfertListWithQuantityToInvMessage : NetworkMessage() {
	var ids: ArrayList<Int> = ArrayList()
	var qtys: ArrayList<Int> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		ids = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = stream.readVarInt().toInt()
			ids.add(item)
		}
		qtys = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = stream.readVarInt().toInt()
			qtys.add(item)
		}
	}
}