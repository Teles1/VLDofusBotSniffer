package fr.lewon.dofus.bot.sniffer.model.messages.game.guild

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildInfosUpgradeMessage : NetworkMessage() {
	var maxTaxCollectorsCount: Int = 0
	var taxCollectorsCount: Int = 0
	var taxCollectorLifePoints: Int = 0
	var taxCollectorDamagesBonuses: Int = 0
	var taxCollectorPods: Int = 0
	var taxCollectorProspecting: Int = 0
	var taxCollectorWisdom: Int = 0
	var boostPoints: Int = 0
	var spellId: ArrayList<Int> = ArrayList()
	var spellLevel: ArrayList<Int> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		maxTaxCollectorsCount = stream.readUnsignedByte().toInt()
		taxCollectorsCount = stream.readUnsignedByte().toInt()
		taxCollectorLifePoints = stream.readVarShort().toInt()
		taxCollectorDamagesBonuses = stream.readVarShort().toInt()
		taxCollectorPods = stream.readVarShort().toInt()
		taxCollectorProspecting = stream.readVarShort().toInt()
		taxCollectorWisdom = stream.readVarShort().toInt()
		boostPoints = stream.readVarShort().toInt()
		spellId = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readVarShort().toInt()
			spellId.add(item)
		}
		spellLevel = ArrayList()
		for (i in 0 until stream.readUnsignedShort().toInt()) {
			val item = stream.readUnsignedShort().toInt()
			spellLevel.add(item)
		}
	}
	override fun getNetworkMessageId(): Int = 4747
}
