package fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay

import fr.lewon.dofus.bot.sniffer.model.types.game.context.EntityDispositionInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.look.EntityLook
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GameRolePlayMerchantInformations : GameRolePlayNamedActorInformations() {
	var sellType: Int = 0
	var options: ArrayList<HumanOption> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		sellType = stream.readUnsignedByte().toInt()
		options = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = ProtocolTypeManager.getInstance<HumanOption>(stream.readUnsignedShort())
			item.deserialize(stream)
			options.add(item)
		}
	}
}
