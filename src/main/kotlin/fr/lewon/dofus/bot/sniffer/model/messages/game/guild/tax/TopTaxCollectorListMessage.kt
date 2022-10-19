package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax.TaxCollectorInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TopTaxCollectorListMessage : AbstractTaxCollectorListMessage() {
	var isDungeon: Boolean = false
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		isDungeon = stream.readBoolean()
	}
}
