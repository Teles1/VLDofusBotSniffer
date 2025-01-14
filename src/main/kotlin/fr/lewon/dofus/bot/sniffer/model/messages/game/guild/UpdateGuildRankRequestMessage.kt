package fr.lewon.dofus.bot.sniffer.model.messages.game.guild

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.GuildRankInformation
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class UpdateGuildRankRequestMessage : NetworkMessage() {
	lateinit var rank: GuildRankInformation
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		rank = GuildRankInformation()
		rank.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 9753
}
