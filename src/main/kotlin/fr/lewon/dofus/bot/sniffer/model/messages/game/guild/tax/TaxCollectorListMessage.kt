package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax.TaxCollectorFightersInformation
import fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax.TaxCollectorInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorListMessage : AbstractTaxCollectorListMessage() {
	var nbcollectorMax: Int = 0
	var fightersInformations: ArrayList<TaxCollectorFightersInformation> = ArrayList()
	var infoType: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		nbcollectorMax = stream.readUnsignedByte().toInt()
		fightersInformations = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = TaxCollectorFightersInformation()
			item.deserialize(stream)
			fightersInformations.add(item)
		}
		infoType = stream.readUnsignedByte().toInt()
	}
	override fun getNetworkMessageId(): Int = 4775
}
