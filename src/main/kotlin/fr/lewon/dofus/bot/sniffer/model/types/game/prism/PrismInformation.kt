package fr.lewon.dofus.bot.sniffer.model.types.game.prism

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismInformation : NetworkType() {
	var typeId: Int = 0
	var state: Int = 0
	var nextVulnerabilityDate: Int = 0
	var placementDate: Int = 0
	var rewardTokenCount: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		typeId = stream.readUnsignedByte().toInt()
		state = stream.readUnsignedByte().toInt()
		nextVulnerabilityDate = stream.readInt().toInt()
		placementDate = stream.readInt().toInt()
		rewardTokenCount = stream.readVarInt().toInt()
	}
}
