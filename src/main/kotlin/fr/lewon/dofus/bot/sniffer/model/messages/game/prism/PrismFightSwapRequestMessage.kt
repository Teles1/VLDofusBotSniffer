package fr.lewon.dofus.bot.sniffer.model.messages.game.prism

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismFightSwapRequestMessage : NetworkMessage() {
	var subAreaId: Int = 0
	var targetId: Double = 0.0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		subAreaId = stream.readVarShort().toInt()
		targetId = stream.readVarLong().toDouble()
	}
	override fun getNetworkMessageId(): Int = 5236
}
