package fr.lewon.dofus.bot.sniffer.model.types.fight.result.data

import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class FightResultExperienceData : FightResultAdditionalData() {

    var experience = 0L
    var showExperience = false
    var experienceLevelFloor = 0L
    var showExperienceLevelFloor = false
    var experienceNextLevelFloor = 0L
    var showExperienceNextLevelFloor = false
    var experienceFightDelta = 0L
    var showExperienceFightDelta = false
    var experienceForGuild = 0L
    var showExperienceForGuild = false
    var experienceForMount = 0L
    var showExperienceForMount = false
    var isIncarnationExperience = false
    var rerollExperienceMul = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        val box = stream.readByte()
        showExperience = BooleanByteWrapper.getFlag(box, 0)
        showExperienceLevelFloor = BooleanByteWrapper.getFlag(box, 1)
        showExperienceNextLevelFloor = BooleanByteWrapper.getFlag(box, 2)
        showExperienceFightDelta = BooleanByteWrapper.getFlag(box, 3)
        showExperienceForGuild = BooleanByteWrapper.getFlag(box, 4)
        showExperienceForMount = BooleanByteWrapper.getFlag(box, 5)
        isIncarnationExperience = BooleanByteWrapper.getFlag(box, 6)
        experience = stream.readVarLong()
        experienceLevelFloor = stream.readVarLong()
        experienceNextLevelFloor = stream.readVarLong()
        experienceFightDelta = stream.readVarLong()
        experienceForGuild = stream.readVarLong()
        experienceForMount = stream.readVarLong()
        rerollExperienceMul = stream.readUnsignedByte()
    }
}