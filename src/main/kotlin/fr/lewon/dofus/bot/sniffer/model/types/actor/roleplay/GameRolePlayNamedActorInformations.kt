package fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay

import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader

open class GameRolePlayNamedActorInformations : GameRolePlayActorInformations() {

    lateinit var name: String

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        name = stream.readUTF()
    }
}