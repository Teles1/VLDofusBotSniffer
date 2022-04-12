package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class GameActionFightExchangePositionsMessage : AbstractGameActionMessage() {

    var targetId = 0.0
    var casterCellId = 0
    var targetCellId = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        targetId = stream.readDouble()
        casterCellId = stream.readUnsignedShort()
        targetCellId = stream.readUnsignedShort()
    }
}