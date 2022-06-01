package fr.lewon.dofus.bot.sniffer.model.types.fight.result

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class FightLoot : INetworkType {

    val objects = ArrayList<Int>()
    var kamas = 0L

    override fun deserialize(stream: ByteArrayReader) {
        for (i in 0 until stream.readUnsignedShort()) {
            objects.add(stream.readVarInt())
        }
        kamas = stream.readVarLong()
    }
}