package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax.TaxCollectorInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorMovementAddMessage : NetworkMessage() {
	lateinit var informations: TaxCollectorInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		informations = ProtocolTypeManager.getInstance<TaxCollectorInformations>(stream.readUnsignedShort())
		informations.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 5732
}
