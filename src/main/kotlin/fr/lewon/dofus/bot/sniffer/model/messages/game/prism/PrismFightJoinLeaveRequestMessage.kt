package fr.lewon.dofus.bot.sniffer.model.messages.game.prism

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismFightJoinLeaveRequestMessage : NetworkMessage() {
	var subAreaId: Int = 0
	var join: Boolean = false
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		subAreaId = stream.readVarShort().toInt()
		join = stream.readBoolean()
	}
}