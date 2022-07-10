package fr.lewon.dofus.bot.sniffer.model.types.actor.human.options

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class HumanOptionSpeedMultiplier : HumanOption() {

    var speedMultiplier = 0

    override fun deserialize(stream: ByteArrayReader) {
        speedMultiplier = stream.readVarInt()
    }
}