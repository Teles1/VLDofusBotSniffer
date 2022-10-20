package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance.summary

import fr.lewon.dofus.bot.sniffer.model.messages.game.PaginationAnswerAbstractMessage
import fr.lewon.dofus.bot.sniffer.model.types.game.social.AllianceFactSheetInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceSummaryMessage : PaginationAnswerAbstractMessage() {
	var alliances: ArrayList<AllianceFactSheetInformations> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		alliances = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = AllianceFactSheetInformations()
			item.deserialize(stream)
			alliances.add(item)
		}
	}
}
