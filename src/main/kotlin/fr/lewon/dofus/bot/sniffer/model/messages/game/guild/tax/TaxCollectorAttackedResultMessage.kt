package fr.lewon.dofus.bot.sniffer.model.messages.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicGuildInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax.TaxCollectorBasicInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorAttackedResultMessage : NetworkMessage() {
	var deadOrAlive: Boolean = false
	lateinit var basicInfos: TaxCollectorBasicInformations
	lateinit var guild: BasicGuildInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		deadOrAlive = stream.readBoolean()
		basicInfos = TaxCollectorBasicInformations()
		basicInfos.deserialize(stream)
		guild = BasicGuildInformations()
		guild.deserialize(stream)
	}
	override fun getNetworkMessageId(): Int = 9933
}
