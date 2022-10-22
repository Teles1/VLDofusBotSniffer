package fr.lewon.dofus.bot.sniffer.model.messages.game.guild

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.GuildMember
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildInformationsMemberUpdateMessage : NetworkMessage() {
	lateinit var member: GuildMember
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		member = GuildMember()
		member.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 3427
}
