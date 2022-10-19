package fr.lewon.dofus.bot.sniffer.model.types.game.context

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.GuildInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorStaticInformations : NetworkType() {
	var firstNameId: Int = 0
	var lastNameId: Int = 0
	lateinit var guildIdentity: GuildInformations
	var callerId: Double = 0.0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		firstNameId = stream.readVarShort().toInt()
		lastNameId = stream.readVarShort().toInt()
		guildIdentity = GuildInformations()
		guildIdentity.deserialize(stream)
		callerId = stream.readVarLong().toDouble()
	}
}
