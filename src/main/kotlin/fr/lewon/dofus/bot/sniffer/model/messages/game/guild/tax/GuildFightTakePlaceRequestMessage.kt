package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildFightTakePlaceRequestMessage : GuildFightJoinRequestMessage() {
	var replacedCharacterId: Double = 0.0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		replacedCharacterId = stream.readVarLong().toDouble()
	}
	override fun getNetworkMessageId(): Int = 1733
}
