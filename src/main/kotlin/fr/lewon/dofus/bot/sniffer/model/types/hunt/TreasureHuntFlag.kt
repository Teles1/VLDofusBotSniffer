package fr.lewon.dofus.bot.sniffer.model.types.hunt

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.manager.DofusMapManager
import fr.lewon.dofus.bot.core.model.maps.DofusMap
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class TreasureHuntFlag : INetworkType {

    lateinit var map: DofusMap
    var state = -1

    override fun deserialize(stream: ByteArrayReader) {
        map = DofusMapManager.getDofusMap(stream.readDouble())
        state = stream.readByte().toInt()
    }
}