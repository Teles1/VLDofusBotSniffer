package fr.lewon.dofus.bot.sniffer.model.messages.game.startup

import fr.lewon.dofus.bot.sniffer.model.types.game.startup.StartupActionAddObject
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class StartupActionAddMessage : NetworkMessage() {
	lateinit var newAction: StartupActionAddObject
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		newAction = StartupActionAddObject()
		newAction.deserialize(stream)
	}
}
