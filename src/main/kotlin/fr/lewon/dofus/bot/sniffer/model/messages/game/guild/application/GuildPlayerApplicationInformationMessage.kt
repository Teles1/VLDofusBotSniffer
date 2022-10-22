package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.application

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.GuildInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.guild.application.GuildApplicationInformation
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildPlayerApplicationInformationMessage : GuildPlayerApplicationAbstractMessage() {
	lateinit var guildInformation: GuildInformations
	lateinit var apply: GuildApplicationInformation
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		guildInformation = GuildInformations()
		guildInformation.deserialize(stream)
		apply = GuildApplicationInformation()
		apply.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 7037
}
