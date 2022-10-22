package fr.lewon.dofus.bot.sniffer.model.messages.game.approach

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ReloginTokenStatusMessage : NetworkMessage() {
	var validToken: Boolean = false
	var ticket: ArrayList<Int> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		validToken = stream.readBoolean()
		ticket = ArrayList()
		for (i in 0 until stream.readVarInt().toInt()) {
			val item = stream.readUnsignedByte().toInt()
			ticket.add(item)
		}
	}
	override fun getNetworkMessageId(): Int = 9030
}
