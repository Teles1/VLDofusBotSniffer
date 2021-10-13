package fr.lewon.dofus.bot.sniffer.model.types.transport

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.manager.DofusMapManager
import fr.lewon.dofus.bot.core.model.maps.DofusMap
import fr.lewon.dofus.bot.sniffer.model.INetworkType

class TeleportDestination : INetworkType {

    var type = 0
    lateinit var map: DofusMap
    var subAreaId = 0
    var level = 0
    var cost = 0

    override fun deserialize(stream: ByteArrayReader) {
        type = stream.readByte().toInt()
        map = DofusMapManager.getDofusMap(stream.readDouble())
        subAreaId = stream.readVarShort()
        level = stream.readVarShort()
        cost = stream.readVarShort()
    }
}