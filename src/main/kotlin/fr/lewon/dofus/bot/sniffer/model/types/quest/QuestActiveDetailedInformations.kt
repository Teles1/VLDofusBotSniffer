package fr.lewon.dofus.bot.sniffer.model.types.quest

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager

class QuestActiveDetailedInformations : QuestActiveInformations() {

    var stepId = 0
    val objectives = ArrayList<QuestObjectiveInformations>()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        stepId = stream.readVarShort()
        for (i in 0 until stream.readUnsignedShort()) {
            val item = TypeManager.getInstance<QuestObjectiveInformations>(stream.readUnsignedShort())
            item.deserialize(stream)
            objectives.add(item)
        }
    }
}