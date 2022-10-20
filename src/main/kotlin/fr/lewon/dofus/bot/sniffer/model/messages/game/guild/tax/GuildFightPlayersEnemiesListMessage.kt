package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.character.CharacterMinimalPlusLookInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildFightPlayersEnemiesListMessage : NetworkMessage() {
	var fightId: Double = 0.0
	var playerInfo: ArrayList<CharacterMinimalPlusLookInformations> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		fightId = stream.readDouble().toDouble()
		playerInfo = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = CharacterMinimalPlusLookInformations()
			item.deserialize(stream)
			playerInfo.add(item)
		}
	}
}
