package fr.lewon.dofus.bot.sniffer.model.types.hunt

import fr.lewon.dofus.bot.model.move.Direction
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.util.manager.d2o.LabelManager

class TreasureHuntStepFollowDirectionToPOI : TreasureHuntStep() {

    lateinit var direction: Direction
    var label = ""

    override fun deserialize(stream: ByteArrayReader) {
        direction = Direction.fromInt(stream.readByte().toInt())
        label = LabelManager.getHintLabel(stream.readVarShort())
    }
}