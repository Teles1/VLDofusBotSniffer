package fr.lewon.dofus.bot.sniffer.model.messages.game.guild

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicGuildInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildInvitedMessage : NetworkMessage() {
	var recruterId: Double = 0.0
	var recruterName: String = ""
	lateinit var guildInfo: BasicGuildInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		recruterId = stream.readVarLong().toDouble()
		recruterName = stream.readUTF()
		guildInfo = BasicGuildInformations()
		guildInfo.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 6176
}
