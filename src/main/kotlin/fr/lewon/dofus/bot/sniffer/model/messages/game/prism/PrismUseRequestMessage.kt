package fr.lewon.dofus.bot.sniffer.model.messages.game.prism

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismUseRequestMessage : NetworkMessage() {
	var moduleToUse: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		moduleToUse = stream.readUnsignedByte().toInt()
	}
}
