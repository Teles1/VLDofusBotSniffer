package fr.lewon.dofus.bot.sniffer.model.types.game.guild

import fr.lewon.dofus.bot.sniffer.model.types.game.character.CharacterMinimalInformations
import fr.lewon.dofus.bot.sniffer.model.types.game.character.guild.note.PlayerNote
import fr.lewon.dofus.bot.sniffer.model.types.game.character.status.PlayerStatus
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class GuildMember : CharacterMinimalInformations() {
	var sex: Boolean = false
	var havenBagShared: Boolean = false
	var breed: Int = 0
	var rankId: Int = 0
	var enrollmentDate: Double = 0.0
	var givenExperience: Double = 0.0
	var experienceGivenPercent: Int = 0
	var connected: Int = 0
	var alignmentSide: Int = 0
	var hoursSinceLastConnection: Int = 0
	var moodSmileyId: Int = 0
	var accountId: Int = 0
	var achievementPoints: Int = 0
	lateinit var status: PlayerStatus
	lateinit var note: PlayerNote
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		val _box0 = stream.readByte()
		sex = BooleanByteWrapper.getFlag(_box0, 0)
		havenBagShared = BooleanByteWrapper.getFlag(_box0, 1)
		breed = stream.readUnsignedByte().toInt()
		rankId = stream.readVarInt().toInt()
		enrollmentDate = stream.readDouble().toDouble()
		givenExperience = stream.readVarLong().toDouble()
		experienceGivenPercent = stream.readUnsignedByte().toInt()
		connected = stream.readUnsignedByte().toInt()
		alignmentSide = stream.readUnsignedByte().toInt()
		hoursSinceLastConnection = stream.readUnsignedShort().toInt()
		moodSmileyId = stream.readVarShort().toInt()
		accountId = stream.readInt().toInt()
		achievementPoints = stream.readInt().toInt()
		status = ProtocolTypeManager.getInstance<PlayerStatus>(stream.readUnsignedShort())
		status.deserialize(stream)
		note = PlayerNote()
		note.deserialize(stream)
	}
}
