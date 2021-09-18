package fr.lewon.dofus.bot.sniffer.model.types.hunt

import fr.lewon.dofus.bot.model.move.Direction
import fr.lewon.dofus.bot.util.d2o.LabelManager
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader

class TreasureHuntStepFollowDirectionToPOI : TreasureHuntStep() {

    lateinit var direction: Direction
    var label = ""

    override fun deserialize(stream: ByteArrayReader) {
        direction = Direction.fromInt(stream.readByte().toInt())
        label = LabelManager.getHintLabel(stream.readVarShort())
    }
}