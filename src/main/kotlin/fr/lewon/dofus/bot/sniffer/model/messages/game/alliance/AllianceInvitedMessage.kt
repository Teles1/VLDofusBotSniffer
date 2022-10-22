package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicNamedAllianceInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceInvitedMessage : NetworkMessage() {
	var recruterId: Double = 0.0
	var recruterName: String = ""
	lateinit var allianceInfo: BasicNamedAllianceInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		recruterId = stream.readVarLong().toDouble()
		recruterName = stream.readUTF()
		allianceInfo = BasicNamedAllianceInformations()
		allianceInfo.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 6994
}
