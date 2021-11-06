package fr.lewon.dofus.bot.sniffer.model.types.character

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class CharacterMinimalInformations : CharacterBasicMinimalInformations() {

    var level = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        level = stream.readVarShort()
    }
}