package fr.lewon.dofus.bot.sniffer.model.types.quest

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class QuestObjectiveInformations : INetworkType {

    var objectiveId = 0
    var active = false
    val dialogParams = ArrayList<String>()

    override fun deserialize(stream: ByteArrayReader) {
        objectiveId = stream.readVarShort()
        active = stream.readBoolean()
        for (i in 0 until stream.readUnsignedShort()) {
            dialogParams.add(stream.readUTF())
        }
    }
}