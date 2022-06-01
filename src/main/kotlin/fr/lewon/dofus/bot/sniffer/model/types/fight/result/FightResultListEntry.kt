package fr.lewon.dofus.bot.sniffer.model.types.fight.result

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class FightResultListEntry : INetworkType {

    var outcome = 0
    var wave = 0
    var rewards = FightLoot()

    override fun deserialize(stream: ByteArrayReader) {
        outcome = stream.readVarShort()
        wave = stream.readUnsignedByte()
        rewards.deserialize(stream)
    }
}