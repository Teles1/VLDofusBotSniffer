package fr.lewon.dofus.bot.sniffer.model.types.game.prism

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismGeolocalizedInformation : PrismSubareaEmptyInfo() {
	var worldX: Int = 0
	var worldY: Int = 0
	var mapId: Double = 0.0
	lateinit var prism: PrismInformation
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		worldX = stream.readUnsignedShort().toInt()
		worldY = stream.readUnsignedShort().toInt()
		mapId = stream.readDouble().toDouble()
		prism = ProtocolTypeManager.getInstance<PrismInformation>(stream.readUnsignedShort())
		prism.deserialize(stream)
	}
}
