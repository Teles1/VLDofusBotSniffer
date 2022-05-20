package fr.lewon.dofus.bot.sniffer.model.types.item

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class ObjectItemInRolePlay : INetworkType {

    var cellId = 0
    var objectGID = 0

    override fun deserialize(stream: ByteArrayReader) {
        cellId = stream.readVarShort()
        objectGID = stream.readVarShort()
    }
}