package fr.lewon.dofus.bot.sniffer.model.types.fight.member

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

open class FightTeamMemberCharacterInformations : FightTeamMemberInformations() {

    lateinit var name: String
    var level = -1

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        name = stream.readUTF()
        level = stream.readVarShort()
    }
}