package fr.lewon.dofus.bot.sniffer.model.messages.game.interactive.meeting

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TeleportToBuddyAnswerMessage : NetworkMessage() {
	var dungeonId: Int = 0
	var buddyId: Double = 0.0
	var accept: Boolean = false
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		dungeonId = stream.readVarShort().toInt()
		buddyId = stream.readVarLong().toDouble()
		accept = stream.readBoolean()
	}
}
