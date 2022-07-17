package fr.lewon.dofus.bot.sniffer.model.types.fight.result

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class FightLoot : INetworkType {

    val objects = ArrayList<FightLootObject>()
    var kamas = 0L

    override fun deserialize(stream: ByteArrayReader) {
        for (i in 0 until stream.readUnsignedShort()) {
            val fightLootObject = FightLootObject()
            fightLootObject.deserialize(stream)
            objects.add(fightLootObject)
        }
        kamas = stream.readVarLong()
    }
}