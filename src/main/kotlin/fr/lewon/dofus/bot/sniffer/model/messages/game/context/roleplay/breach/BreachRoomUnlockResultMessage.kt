package fr.lewon.dofus.bot.sniffer.model.messages.game.context.roleplay.breach

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class BreachRoomUnlockResultMessage : NetworkMessage() {
	var roomId: Int = 0
	var result: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		roomId = stream.readUnsignedByte().toInt()
		result = stream.readUnsignedByte().toInt()
	}
}