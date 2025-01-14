package fr.lewon.dofus.bot.sniffer.model.types.game.guild.tax

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicGuildInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class TaxCollectorGuildInformations : TaxCollectorComplementaryInformations() {
	lateinit var guild: BasicGuildInformations
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		guild = BasicGuildInformations()
		guild.deserialize(stream)
	}
}
