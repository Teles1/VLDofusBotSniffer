package fr.lewon.dofus.bot.sniffer.model.messages.game.context.roleplay.npc

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicGuildInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicNamedAllianceInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceTaxCollectorDialogQuestionExtendedMessage : TaxCollectorDialogQuestionExtendedMessage() {
	lateinit var alliance: BasicNamedAllianceInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		alliance = BasicNamedAllianceInformations()
		alliance.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 6708
}
