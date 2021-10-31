package fr.lewon.dofus.bot.sniffer.model.types.fight.charac.impl

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class CharacterUsableCharacteristicDetailed : CharacterCharacteristicDetailed() {

    var used = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        used = stream.readVarShort()
    }
}