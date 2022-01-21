package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.d2o.managers.MapManager
import fr.lewon.dofus.bot.core.model.maps.DofusMap
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class CurrentMapMessage : INetworkMessage {

    lateinit var map: DofusMap
    var mapKey = ""

    override fun deserialize(stream: ByteArrayReader) {
        map = MapManager.getDofusMap(stream.readDouble())
        mapKey = stream.readUTF()
    }
}