package fr.lewon.dofus.bot.sniffer.model.types.fight.result.entry

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.types.fight.result.data.FightResultAdditionalData

class FightResultPlayerListEntry : FightResultFighterListEntry() {

    var level = 0
    val additional = ArrayList<FightResultAdditionalData>()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        level = stream.readVarShort()
        for (i in 0 until stream.readUnsignedShort()) {
            val additionalData: FightResultAdditionalData = TypeManager.getInstance(stream.readUnsignedShort())
            additionalData.deserialize(stream)
            additional.add(additionalData)
        }
    }
}