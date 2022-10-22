package fr.lewon.dofus.bot.sniffer.model.messages.game.ui

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ClientUIOpenedByObjectMessage : ClientUIOpenedMessage() {
	var uid: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		uid = stream.readVarInt().toInt()
	}
	override fun getNetworkMessageId(): Int = 5729
}
