package fr.lewon.dofus.bot.sniffer.model.messages.game.pvp

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class UpdateMapPlayersAgressableStatusMessage : NetworkMessage() {
	var playerIds: ArrayList<Double> = ArrayList()
	var enable: ArrayList<Int> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		playerIds = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readVarLong().toDouble()
			playerIds.add(item)
		}
		enable = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readUnsignedByte().toInt()
			enable.add(item)
		}
	}
	override fun getNetworkMessageId(): Int = 8920
}
