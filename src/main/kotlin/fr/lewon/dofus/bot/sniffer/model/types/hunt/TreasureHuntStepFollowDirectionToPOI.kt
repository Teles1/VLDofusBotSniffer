package fr.lewon.dofus.bot.sniffer.model.types.hunt

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.model.move.Direction

class TreasureHuntStepFollowDirectionToPOI : TreasureHuntStep() {

    lateinit var direction: Direction
    var poiLabelId = 0

    override fun deserialize(stream: ByteArrayReader) {
        direction = Direction.fromInt(stream.readByte().toInt())
        poiLabelId = stream.readVarShort()
    }
}