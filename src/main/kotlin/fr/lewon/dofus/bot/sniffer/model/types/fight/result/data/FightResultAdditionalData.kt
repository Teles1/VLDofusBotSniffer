package fr.lewon.dofus.bot.sniffer.model.types.fight.result.data

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class FightResultAdditionalData : INetworkType {
    override fun deserialize(stream: ByteArrayReader) {
    }
}