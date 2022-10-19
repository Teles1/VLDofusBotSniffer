package fr.lewon.dofus.bot.sniffer.model.types.game.context.fight

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicGuildInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class FightResultTaxCollectorListEntry : FightResultFighterListEntry() {
	var level: Int = 0
	lateinit var guildInfo: BasicGuildInformations
	var experienceForGuild: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		level = stream.readUnsignedByte().toInt()
		guildInfo = BasicGuildInformations()
		guildInfo.deserialize(stream)
		experienceForGuild = stream.readInt().toInt()
	}
}
