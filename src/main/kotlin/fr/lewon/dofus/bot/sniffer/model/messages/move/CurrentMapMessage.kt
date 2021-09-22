package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.model.maps.DofusMap
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.util.manager.d2o.DofusMapManager

class CurrentMapMessage : INetworkMessage {

    lateinit var map: DofusMap
    var mapKey = ""

    override fun deserialize(stream: ByteArrayReader) {
        map = DofusMapManager.getDofusMap(stream.readDouble())
        mapKey = stream.readUTF()
    }
}