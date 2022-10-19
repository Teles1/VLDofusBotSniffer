package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance.summary

import fr.lewon.dofus.bot.sniffer.model.messages.game.PaginationRequestAbstractMessage
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceSummaryRequestMessage : PaginationRequestAbstractMessage() {
	var nameFilter: String = ""
	var tagFilter: String = ""
	var playerNameFilter: String = ""
	var sortType: Int = 0
	var sortDescending: Boolean = false
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		nameFilter = stream.readUTF()
		tagFilter = stream.readUTF()
		playerNameFilter = stream.readUTF()
		sortType = stream.readUnsignedByte().toInt()
		sortDescending = stream.readBoolean()
	}
}
