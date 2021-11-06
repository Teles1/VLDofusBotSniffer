package fr.lewon.dofus.bot.sniffer.model.types.character

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

open class CharacterBasicMinimalInformations : AbstractCharacterInformation() {

    var name = ""

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        name = stream.readUTF()
    }

}