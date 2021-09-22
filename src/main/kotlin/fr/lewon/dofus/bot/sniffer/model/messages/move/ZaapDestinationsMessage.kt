package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.model.maps.DofusMap
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.transport.TeleportDestination
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.util.manager.d2o.DofusMapManager

class ZaapDestinationsMessage : INetworkMessage {

    var type = -1
    var destinations = ArrayList<TeleportDestination>()
    lateinit var spawnMap: DofusMap

    override fun deserialize(stream: ByteArrayReader) {
        type = stream.readByte().toInt()
        for (i in 0 until stream.readUnsignedShort()) {
            val destination = TeleportDestination()
            destination.deserialize(stream)
            destinations.add(destination)
        }
        spawnMap = DofusMapManager.getDofusMap(stream.readDouble())
    }

}