package fr.lewon.dofus.bot.sniffer.model.types.quest

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class QuestActiveInformations : INetworkType {

    var questId = 0

    override fun deserialize(stream: ByteArrayReader) {
        questId = stream.readVarShort()
    }
}