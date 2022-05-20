package fr.lewon.dofus.bot.sniffer.model.types.actor

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook
import fr.lewon.dofus.bot.sniffer.model.types.fight.disposition.EntityDispositionInformations

open class GameContextActorInformations : GameContextActorPositionInformations() {

    lateinit var entityLook: EntityLook

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        entityLook = EntityLook()
        entityLook.deserialize(stream)
    }

    fun initGameContextActorInformations(
        contextualId: Double,
        disposition: EntityDispositionInformations,
        look: EntityLook
    ) {
        initGameContextActorPositionInformations(contextualId, disposition)
        this.entityLook = look
    }
}