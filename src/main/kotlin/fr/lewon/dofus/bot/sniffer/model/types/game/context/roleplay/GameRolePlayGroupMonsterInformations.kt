package fr.lewon.dofus.bot.sniffer.model.types.game.context.roleplay

import fr.lewon.dofus.bot.sniffer.model.types.game.context.EntityDispositionInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.look.EntityLook
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GameRolePlayGroupMonsterInformations : GameRolePlayActorInformations() {
	var keyRingBonus: Boolean = false
	var hasHardcoreDrop: Boolean = false
	var hasAVARewardToken: Boolean = false
	lateinit var staticInfos: GroupMonsterStaticInformations
	var lootShare: Int = 0
	var alignmentSide: Int = 0
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		val _box0 = stream.readByte()
		keyRingBonus = BooleanByteWrapper.getFlag(_box0, 0)
		hasHardcoreDrop = BooleanByteWrapper.getFlag(_box0, 1)
		hasAVARewardToken = BooleanByteWrapper.getFlag(_box0, 2)
		staticInfos = ProtocolTypeManager.getInstance<GroupMonsterStaticInformations>(stream.readUnsignedShort())
		staticInfos.deserialize(stream)
		lootShare = stream.readUnsignedByte().toInt()
		alignmentSide = stream.readUnsignedByte().toInt()
	}
}
