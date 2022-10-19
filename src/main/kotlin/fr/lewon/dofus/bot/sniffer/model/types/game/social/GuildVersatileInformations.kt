package fr.lewon.dofus.bot.sniffer.model.types.game.social

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildVersatileInformations : NetworkType() {
	var guildId: Int = 0
	var leaderId: Double = 0.0
	var guildLevel: Int = 0
	var nbMembers: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		guildId = stream.readVarInt().toInt()
		leaderId = stream.readVarLong().toDouble()
		guildLevel = stream.readUnsignedByte().toInt()
		nbMembers = stream.readUnsignedByte().toInt()
	}
}
