package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.AllianceInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceJoinedMessage : NetworkMessage() {
	lateinit var allianceInfo: AllianceInformations
	var enabled: Boolean = false
	var leadingGuildId: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		allianceInfo = AllianceInformations()
		allianceInfo.deserialize(stream)
		enabled = stream.readBoolean()
		leadingGuildId = stream.readVarInt().toInt()
	}
	override fun getNetworkMessageId(): Int = 7127
}
