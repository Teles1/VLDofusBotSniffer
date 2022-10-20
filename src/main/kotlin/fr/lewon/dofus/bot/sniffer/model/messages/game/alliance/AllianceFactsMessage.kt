package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.GuildInAllianceInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.social.AllianceFactSheetInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceFactsMessage : NetworkMessage() {
	lateinit var infos: AllianceFactSheetInformations
	var guilds: ArrayList<GuildInAllianceInformations> = ArrayList()
	var controlledSubareaIds: ArrayList<Int> = ArrayList()
	var leaderCharacterId: Double = 0.0
	var leaderCharacterName: String = ""
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		infos = ProtocolTypeManager.getInstance<AllianceFactSheetInformations>(stream.readUnsignedShort())
		infos.deserialize(stream)
		guilds = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = GuildInAllianceInformations()
			item.deserialize(stream)
			guilds.add(item)
		}
		controlledSubareaIds = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readVarShort().toInt()
			controlledSubareaIds.add(item)
		}
		leaderCharacterId = stream.readVarLong().toDouble()
		leaderCharacterName = stream.readUTF()
	}
}
