package fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.monster

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager

class GameRolePlayGroupMonsterWaveInformations : GameRolePlayGroupMonsterInformations() {

    var nbWaves = 0
    val alternatives = ArrayList<GroupMonsterStaticInformations>()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        nbWaves = stream.readUnsignedByte()
        for (i in 0 until stream.readUnsignedShort()) {
            val item = TypeManager.getInstance<GroupMonsterStaticInformations>(stream.readUnsignedShort())
            item.deserialize(stream)
            alternatives.add(item)
        }
    }
}