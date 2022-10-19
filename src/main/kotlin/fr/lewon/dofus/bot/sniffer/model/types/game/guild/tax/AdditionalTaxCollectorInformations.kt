package fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AdditionalTaxCollectorInformations : NetworkType() {
	var collectorCallerName: String = ""
	var date: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		collectorCallerName = stream.readUTF()
		date = stream.readInt().toInt()
	}
}
