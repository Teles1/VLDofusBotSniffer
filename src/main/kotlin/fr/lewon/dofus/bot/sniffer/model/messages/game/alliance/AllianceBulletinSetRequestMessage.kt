package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance

import fr.lewon.dofus.bot.sniffer.model.messages.game.social.SocialNoticeSetRequestMessage
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceBulletinSetRequestMessage : SocialNoticeSetRequestMessage() {
	var content: String = ""
	var notifyMembers: Boolean = false
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		content = stream.readUTF()
		notifyMembers = stream.readBoolean()
	}
}