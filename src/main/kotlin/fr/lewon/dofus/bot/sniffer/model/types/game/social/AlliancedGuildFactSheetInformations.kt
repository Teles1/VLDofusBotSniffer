package fr.lewon.dofus.bot.sniffer.model.types.game.social

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicNamedAllianceInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.GuildInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.guild.GuildEmblem
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AlliancedGuildFactSheetInformations : GuildInformations() {
	lateinit var allianceInfos: BasicNamedAllianceInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		allianceInfos = BasicNamedAllianceInformations()
		allianceInfos.deserialize(stream)
	}
}
