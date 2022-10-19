package fr.lewon.dofus.bot.sniffer.model.messages.game.prism

import fr.lewon.dofus.bot.sniffer.model.types.game.prism.PrismFightersInformation
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class PrismsInfoValidMessage : NetworkMessage() {
	var fights: ArrayList<PrismFightersInformation> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		fights = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = PrismFightersInformation()
			item.deserialize(stream)
			fights.add(item)
		}
	}
}
