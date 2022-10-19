package fr.lewon.dofus.bot.sniffer.model.types.game.prism

import fr.lewon.dofus.bot.sniffer.model.types.game.character.CharacterMinimalPlusLookInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.fight.ProtectedEntityWaitingForHelpInfo
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismFightersInformation : NetworkType() {
	var subAreaId: Int = 0
	lateinit var waitingForHelpInfo: ProtectedEntityWaitingForHelpInfo
	var allyCharactersInformations: ArrayList<CharacterMinimalPlusLookInformations> = ArrayList()
	var enemyCharactersInformations: ArrayList<CharacterMinimalPlusLookInformations> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		subAreaId = stream.readVarShort().toInt()
		waitingForHelpInfo = ProtectedEntityWaitingForHelpInfo()
		waitingForHelpInfo.deserialize(stream)
		allyCharactersInformations = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = ProtocolTypeManager.getInstance<CharacterMinimalPlusLookInformations>(stream.readUnsignedShort())
			item.deserialize(stream)
			allyCharactersInformations.add(item)
		}
		enemyCharactersInformations = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = ProtocolTypeManager.getInstance<CharacterMinimalPlusLookInformations>(stream.readUnsignedShort())
			item.deserialize(stream)
			enemyCharactersInformations.add(item)
		}
	}
}
