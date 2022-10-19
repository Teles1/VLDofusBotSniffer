package fr.lewon.dofus.bot.sniffer.model.types.game.prism

import fr.lewon.dofus.bot.sniffer.model.types.game.data.items.ObjectItem
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class AllianceInsiderPrismInformation : PrismInformation() {
	var lastTimeSlotModificationDate: Int = 0
	var lastTimeSlotModificationAuthorGuildId: Int = 0
	var lastTimeSlotModificationAuthorId: Double = 0.0
	var lastTimeSlotModificationAuthorName: String = ""
	var modulesObjects: ArrayList<ObjectItem> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		lastTimeSlotModificationDate = stream.readInt().toInt()
		lastTimeSlotModificationAuthorGuildId = stream.readVarInt().toInt()
		lastTimeSlotModificationAuthorId = stream.readVarLong().toDouble()
		lastTimeSlotModificationAuthorName = stream.readUTF()
		modulesObjects = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = ObjectItem()
			item.deserialize(stream)
			modulesObjects.add(item)
		}
	}
}
