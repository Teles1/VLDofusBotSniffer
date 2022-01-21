package fr.lewon.dofus.bot.sniffer.model.messages.quest

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.quest.QuestActiveInformations

class QuestListMessage : INetworkMessage {

    val finishedQuestsIds = ArrayList<Int>()
    val finishedQuestsCounts = ArrayList<Int>()
    val activeQuests = ArrayList<QuestActiveInformations>()
    val reinitDoneQuestsIds = ArrayList<Int>()

    override fun deserialize(stream: ByteArrayReader) {
        for (i in 0 until stream.readUnsignedShort()) {
            finishedQuestsIds.add(stream.readVarShort())
        }
        for (i in 0 until stream.readUnsignedShort()) {
            finishedQuestsCounts.add(stream.readVarShort())
        }
        for (i in 0 until stream.readUnsignedShort()) {
            val item = TypeManager.getInstance<QuestActiveInformations>(stream.readUnsignedShort())
            item.deserialize(stream)
            activeQuests.add(item)
        }
        for (i in 0 until stream.readUnsignedShort()) {
            reinitDoneQuestsIds.add(stream.readVarShort())
        }
    }
}