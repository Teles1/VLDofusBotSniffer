package fr.lewon.dofus.bot.sniffer.model.messages.game.prism

import fr.lewon.dofus.bot.sniffer.model.types.game.character.CharacterMinimalPlusLookInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismFightDefenderAddMessage : NetworkMessage() {
	var subAreaId: Int = 0
	var fightId: Int = 0
	lateinit var defender: CharacterMinimalPlusLookInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		subAreaId = stream.readVarShort().toInt()
		fightId = stream.readVarShort().toInt()
		defender = ProtocolTypeManager.getInstance<CharacterMinimalPlusLookInformations>(stream.readUnsignedShort())
		defender.deserialize(stream)
	}
}
