package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax.TaxCollectorInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AbstractTaxCollectorListMessage : NetworkMessage() {
	var informations: ArrayList<TaxCollectorInformations> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		informations = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = ProtocolTypeManager.getInstance<TaxCollectorInformations>(stream.readUnsignedShort())
			item.deserialize(stream)
			informations.add(item)
		}
	}
	override fun getNetworkMessageId(): Int = 7262
}
