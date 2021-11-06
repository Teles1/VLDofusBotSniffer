package fr.lewon.dofus.bot.sniffer.model.types.character

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class AbstractCharacterInformation : INetworkType {

    var id = 0L

    override fun deserialize(stream: ByteArrayReader) {
        id = stream.readVarLong()
    }

}