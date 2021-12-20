package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.types.fight.summon.GameContextSummonsInformation

class GameActionFightMultipleSummonMessage : AbstractGameActionMessage() {

    val summons = ArrayList<GameContextSummonsInformation>()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        for (i in 0 until stream.readShort()) {
            val summon = TypeManager.getInstance<GameContextSummonsInformation>(stream.readUnsignedShort())
            summon.deserialize(stream)
            summons.add(summon)
        }
    }

}