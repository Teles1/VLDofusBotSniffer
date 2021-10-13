package fr.lewon.dofus.bot.sniffer.model.types.hunt

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.model.move.Direction

class TreasureHuntStepFollowDirectionToHint : TreasureHuntStep() {

    lateinit var direction: Direction
    var npcId = 0

    override fun deserialize(stream: ByteArrayReader) {
        direction = Direction.fromInt(stream.readByte().toInt())
        npcId = stream.readVarShort()
    }
}