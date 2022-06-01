package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class ExchangeStartedWithPodsMessage : ExchangeStartedMessage() {

    var firstCharacterId = 0.0
    var firstCharacterCurrentWeight = 0
    var firstCharacterMaxWeight = 0
    var secondCharacterId = 0.0
    var secondCharacterCurrentWeight = 0
    var secondCharacterMaxWeight = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        firstCharacterId = stream.readDouble()
        firstCharacterCurrentWeight = stream.readVarInt()
        firstCharacterMaxWeight = stream.readVarInt()
        secondCharacterId = stream.readDouble()
        secondCharacterCurrentWeight = stream.readVarInt()
        secondCharacterMaxWeight = stream.readVarInt()
    }
}