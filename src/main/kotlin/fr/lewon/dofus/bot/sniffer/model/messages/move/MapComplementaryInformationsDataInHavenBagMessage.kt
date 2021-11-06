package fr.lewon.dofus.bot.sniffer.model.messages.move

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.character.CharacterMinimalInformations

class MapComplementaryInformationsDataInHavenBagMessage : MapComplementaryInformationsDataMessage() {

    var ownerInformations = CharacterMinimalInformations()
    var theme = 0
    var roomId = 0
    var maxRoomId = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        ownerInformations.deserialize(stream)
        theme = stream.readUnsignedByte()
        roomId = stream.readUnsignedByte()
        maxRoomId = stream.readUnsignedByte()
    }
}