package fr.lewon.dofus.bot.sniffer.model.messages.fight

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook

class GameActionFightChangeLookMessage : AbstractGameActionMessage() {

    var targetId = 0.0
    val entityLook = EntityLook()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        targetId = stream.readDouble()
        entityLook.deserialize(stream)
    }
}