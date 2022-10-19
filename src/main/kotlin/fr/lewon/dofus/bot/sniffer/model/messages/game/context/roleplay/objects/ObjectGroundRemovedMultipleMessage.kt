package fr.lewon.dofus.bot.sniffer.model.messages.game.context.roleplay.objects

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class ObjectGroundRemovedMultipleMessage : NetworkMessage() {
	var cells: ArrayList<Int> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		cells = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = stream.readVarShort().toInt()
			cells.add(item)
		}
	}
}