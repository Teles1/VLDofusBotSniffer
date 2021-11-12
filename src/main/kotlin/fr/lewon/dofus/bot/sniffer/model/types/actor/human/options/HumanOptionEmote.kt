package fr.lewon.dofus.bot.sniffer.model.types.actor.human.options

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class HumanOptionEmote : HumanOption() {

    var emoteId = -1
    var emoteStartTime = -1.0

    override fun deserialize(stream: ByteArrayReader) {
        emoteId = stream.readUnsignedShort()
        emoteStartTime = stream.readDouble()
    }
}