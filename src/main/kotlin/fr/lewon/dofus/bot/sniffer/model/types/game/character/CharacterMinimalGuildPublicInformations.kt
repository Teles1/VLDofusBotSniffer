package fr.lewon.dofus.bot.sniffer.model.types.game.character

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.GuildRankPublicInformation
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class CharacterMinimalGuildPublicInformations : CharacterMinimalInformations() {
	lateinit var rank: GuildRankPublicInformation
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		rank = GuildRankPublicInformation()
		rank.deserialize(stream)
	}
}
