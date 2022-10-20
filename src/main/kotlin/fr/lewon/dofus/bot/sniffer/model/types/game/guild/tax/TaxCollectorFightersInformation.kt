package fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.character.CharacterMinimalPlusLookInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorFightersInformation : NetworkType() {
	var collectorId: Double = 0.0
	var allyCharactersInformations: ArrayList<CharacterMinimalPlusLookInformations> = ArrayList()
	var enemyCharactersInformations: ArrayList<CharacterMinimalPlusLookInformations> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		collectorId = stream.readDouble().toDouble()
		allyCharactersInformations = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = ProtocolTypeManager.getInstance<CharacterMinimalPlusLookInformations>(stream.readUnsignedShort())
			item.deserialize(stream)
			allyCharactersInformations.add(item)
		}
		enemyCharactersInformations = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = ProtocolTypeManager.getInstance<CharacterMinimalPlusLookInformations>(stream.readUnsignedShort())
			item.deserialize(stream)
			enemyCharactersInformations.add(item)
		}
	}
}
