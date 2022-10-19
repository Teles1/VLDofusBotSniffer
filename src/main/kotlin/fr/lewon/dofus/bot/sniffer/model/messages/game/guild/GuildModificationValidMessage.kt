package fr.lewon.dofus.bot.sniffer.model.messages.game.guild

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.GuildEmblem
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildModificationValidMessage : NetworkMessage() {
	var guildName: String = ""
	lateinit var guildEmblem: GuildEmblem
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		guildName = stream.readUTF()
		guildEmblem = GuildEmblem()
		guildEmblem.deserialize(stream)
	}
}