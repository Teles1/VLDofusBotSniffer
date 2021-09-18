package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.model.maps.DofusMap
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.util.d2o.DofusMapManager
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader

class CurrentMapMessage : INetworkMessage {

    lateinit var map: DofusMap
    var mapKey = ""

    override fun deserialize(stream: ByteArrayReader) {
        map = DofusMapManager.getDofusMap(stream.readDouble())
        mapKey = stream.readUTF()
    }
}