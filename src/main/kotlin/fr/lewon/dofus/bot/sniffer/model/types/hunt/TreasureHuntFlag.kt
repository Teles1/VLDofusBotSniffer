package fr.lewon.dofus.bot.sniffer.model.types.hunt

import fr.lewon.dofus.bot.model.maps.DofusMap
import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.util.manager.d2o.DofusMapManager

class TreasureHuntFlag : INetworkType {

    lateinit var map: DofusMap
    var state = -1

    override fun deserialize(stream: ByteArrayReader) {
        map = DofusMapManager.getDofusMap(stream.readDouble())
        state = stream.readByte().toInt()
    }
}