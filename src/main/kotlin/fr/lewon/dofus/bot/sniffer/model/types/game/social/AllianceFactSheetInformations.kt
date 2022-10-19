package fr.lewon.dofus.bot.sniffer.model.types.game.social

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.AllianceInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.guild.GuildEmblem
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceFactSheetInformations : AllianceInformations() {
	var creationDate: Int = 0
	var nbGuilds: Int = 0
	var nbMembers: Int = 0
	var nbSubarea: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		creationDate = stream.readInt().toInt()
		nbGuilds = stream.readVarShort().toInt()
		nbMembers = stream.readVarShort().toInt()
		nbSubarea = stream.readVarShort().toInt()
	}
}
