package fr.lewon.dofus.bot.sniffer.model.types.quest

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class QuestObjectiveInformationsWithCompletion : QuestObjectiveInformations() {

    var curCompletion = 0
    var maxCompletion = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        curCompletion = stream.readVarShort()
        maxCompletion = stream.readVarShort()
    }
}