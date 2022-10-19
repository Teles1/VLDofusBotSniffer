package fr.lewon.dofus.bot.sniffer.model.types.game.social

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.GuildEmblem
import fr.lewon.dofus.bot.sniffer.model.types.game.guild.recruitment.GuildRecruitmentInformation
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildInsiderFactSheetInformations : GuildFactSheetInformations() {
	var leaderName: String = ""
	var nbConnectedMembers: Int = 0
	var nbTaxCollectors: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		leaderName = stream.readUTF()
		nbConnectedMembers = stream.readVarShort().toInt()
		nbTaxCollectors = stream.readUnsignedByte().toInt()
	}
}
