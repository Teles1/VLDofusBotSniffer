package fr.lewon.dofus.bot.sniffer.model.messages.game.inventory.exchanges

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ExchangeReplyTaxVendorMessage : NetworkMessage() {
	var objectValue: Double = 0.0
	var totalTaxValue: Double = 0.0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		objectValue = stream.readVarLong().toDouble()
		totalTaxValue = stream.readVarLong().toDouble()
	}
	override fun getNetworkMessageId(): Int = 6703
}
