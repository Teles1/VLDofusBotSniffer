package fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class SpawnInformation : INetworkType {
    override fun deserialize(stream: ByteArrayReader) {
    }
}