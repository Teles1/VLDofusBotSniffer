package fr.lewon.dofus.bot.sniffer.model.messages.game.guild

import fr.lewon.dofus.bot.sniffer.model.types.game.character.CharacterMinimalGuildPublicInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicNamedAllianceInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.social.GuildFactSheetInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildInAllianceFactsMessage : GuildFactsMessage() {
	lateinit var allianceInfos: BasicNamedAllianceInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		allianceInfos = BasicNamedAllianceInformations()
		allianceInfos.deserialize(stream)
	}
}
