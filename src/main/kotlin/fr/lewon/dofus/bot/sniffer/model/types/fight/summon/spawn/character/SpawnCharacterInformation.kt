package fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.character

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.SpawnInformation

class SpawnCharacterInformation : SpawnInformation() {

    var name = ""
    var level = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        name = stream.readUTF()
        level = stream.readVarShort()
    }
}