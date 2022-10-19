package fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorLootInformations : TaxCollectorComplementaryInformations() {
	var kamas: Double = 0.0
	var experience: Double = 0.0
	var pods: Int = 0
	var itemsValue: Double = 0.0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		kamas = stream.readVarLong().toDouble()
		experience = stream.readVarLong().toDouble()
		pods = stream.readVarInt().toInt()
		itemsValue = stream.readVarLong().toDouble()
	}
}
