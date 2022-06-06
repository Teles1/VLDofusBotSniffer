package fr.lewon.dofus.bot.sniffer.model.types.fight.charac

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.sniffer.model.TypeManager

class CharacterCharacteristics : INetworkType {

    var characteristics = ArrayList<CharacterCharacteristic>()

    override fun deserialize(stream: ByteArrayReader) {
        for (i in 0 until stream.readUnsignedShort()) {
            val characteristic = TypeManager.getInstance<CharacterCharacteristic>(stream.readUnsignedShort())
            characteristic.deserialize(stream)
            characteristics.add(characteristic)
        }
    }
}