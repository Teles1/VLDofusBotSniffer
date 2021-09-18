package fr.lewon.dofus.bot.sniffer.model.types.hunt

import fr.lewon.dofus.bot.model.move.Direction
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader

class TreasureHuntStepFollowDirectionToHint : TreasureHuntStep() {

    lateinit var direction: Direction
    var npcId = 0

    override fun deserialize(stream: ByteArrayReader) {
        direction = Direction.fromInt(stream.readByte().toInt())
        npcId = stream.readVarShort()
    }
}