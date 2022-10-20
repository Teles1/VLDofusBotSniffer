package fr.lewon.dofus.bot.sniffer.model.messages.game.alliance

import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.AllianceInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay.BasicAllianceInformations
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class KohUpdateMessage : NetworkMessage() {
	var alliances: ArrayList<AllianceInformations> = ArrayList()
	var allianceNbMembers: ArrayList<Int> = ArrayList()
	var allianceRoundWeigth: ArrayList<Int> = ArrayList()
	var allianceMatchScore: ArrayList<Int> = ArrayList()
	var allianceMapWinners: ArrayList<BasicAllianceInformations> = ArrayList()
	var allianceMapWinnerScore: Int = 0
	var allianceMapMyAllianceScore: Int = 0
	var nextTickTime: Double = 0.0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		alliances = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = AllianceInformations()
			item.deserialize(stream)
			alliances.add(item)
		}
		allianceNbMembers = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readVarShort().toInt()
			allianceNbMembers.add(item)
		}
		allianceRoundWeigth = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readVarInt().toInt()
			allianceRoundWeigth.add(item)
		}
		allianceMatchScore = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readUnsignedByte().toInt()
			allianceMatchScore.add(item)
		}
		allianceMapWinners = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = BasicAllianceInformations()
			item.deserialize(stream)
			allianceMapWinners.add(item)
		}
		allianceMapWinnerScore = stream.readVarInt().toInt()
		allianceMapMyAllianceScore = stream.readVarInt().toInt()
		nextTickTime = stream.readDouble().toDouble()
	}
}
