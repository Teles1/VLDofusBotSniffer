package fr.lewon.dofus.bot.sniffer.model.types.game.character

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicAllianceInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicGuildInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.look.EntityLook
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class CharacterMinimalAllianceInformations : CharacterMinimalGuildInformations() {
	lateinit var alliance: BasicAllianceInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		alliance = BasicAllianceInformations()
		alliance.deserialize(stream)
	}
}
