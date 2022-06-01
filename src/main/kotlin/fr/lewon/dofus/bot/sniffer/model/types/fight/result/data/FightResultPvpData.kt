package fr.lewon.dofus.bot.sniffer.model.types.fight.result.data

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class FightResultPvpData : FightResultAdditionalData() {

    var grade = 0
    var minHonorForGrade = 0
    var maxHonorForGrade = 0
    var honor = 0
    var honorDelta = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        grade = stream.readUnsignedByte()
        minHonorForGrade = stream.readVarShort()
        maxHonorForGrade = stream.readVarShort()
        honor = stream.readVarShort()
        honorDelta = stream.readVarShort()
    }
}