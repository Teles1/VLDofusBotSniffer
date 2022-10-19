package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance

import fr.lewon.dofus.bot.sniffer.model.types.game.prism.PrismSubareaEmptyInfo
import fr.lewon.dofus.bot.sniffer.model.types.game.social.AllianceFactSheetInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.social.GuildInsiderFactSheetInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceInsiderInfoMessage : NetworkMessage() {
	lateinit var allianceInfos: AllianceFactSheetInformations
	var guilds: ArrayList<GuildInsiderFactSheetInformations> = ArrayList()
	var prisms: ArrayList<PrismSubareaEmptyInfo> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		allianceInfos = AllianceFactSheetInformations()
		allianceInfos.deserialize(stream)
		guilds = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = GuildInsiderFactSheetInformations()
			item.deserialize(stream)
			guilds.add(item)
		}
		prisms = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = ProtocolTypeManager.getInstance<PrismSubareaEmptyInfo>(stream.readUnsignedShort())
			item.deserialize(stream)
			prisms.add(item)
		}
	}
}
