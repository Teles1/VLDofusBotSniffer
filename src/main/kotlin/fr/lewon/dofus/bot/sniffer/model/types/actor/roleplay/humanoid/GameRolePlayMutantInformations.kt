package fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.humanoid

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class GameRolePlayMutantInformations : GameRolePlayHumanoidInformations() {

    var monsterId = 0
    var powerLevel = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        monsterId = stream.readVarShort()
        powerLevel = stream.readUnsignedByte()
    }
}