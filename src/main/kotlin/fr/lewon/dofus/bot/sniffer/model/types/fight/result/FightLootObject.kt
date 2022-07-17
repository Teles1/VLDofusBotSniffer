package fr.lewon.dofus.bot.sniffer.model.types.fight.result

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class FightLootObject : INetworkType {

    var objectId = 0
    var quantity = 0
    var priorityHint = 0

    override fun deserialize(stream: ByteArrayReader) {
        objectId = stream.readInt()
        quantity = stream.readInt()
        priorityHint = stream.readInt()
    }
}