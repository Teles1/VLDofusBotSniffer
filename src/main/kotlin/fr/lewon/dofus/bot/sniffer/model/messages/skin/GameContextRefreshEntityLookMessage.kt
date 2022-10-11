package fr.lewon.dofus.bot.sniffer.model.messages.skin

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook

class GameContextRefreshEntityLookMessage : INetworkMessage {

    var id = 0.0
    var entityLook = EntityLook()

    override fun deserialize(stream: ByteArrayReader) {
        this.id = stream.readDouble()
        this.entityLook.deserialize(stream)
    }
}