package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax.TaxCollectorBasicInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorMovementMessage : NetworkMessage() {
	var movementType: Int = 0
	lateinit var basicInfos: TaxCollectorBasicInformations
	var playerId: Double = 0.0
	var playerName: String = ""
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		movementType = stream.readUnsignedByte().toInt()
		basicInfos = TaxCollectorBasicInformations()
		basicInfos.deserialize(stream)
		playerId = stream.readVarLong().toDouble()
		playerName = stream.readUTF()
	}
}
