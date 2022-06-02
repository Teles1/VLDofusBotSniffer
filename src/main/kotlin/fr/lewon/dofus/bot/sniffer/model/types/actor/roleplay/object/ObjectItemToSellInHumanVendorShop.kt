package fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.effect.ObjectEffect

class ObjectItemToSellInHumanVendorShop : Item() {

    var objectGID = 0
    var effects = ArrayList<ObjectEffect>()
    var objectUID = 0
    var quantity = 0
    var objectPrice = 0L
    var publicPrice = 0L

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        objectGID = stream.readVarShort()
        for (i in 0 until stream.readUnsignedShort()) {
            val objectEffect = TypeManager.getInstance<ObjectEffect>(stream.readUnsignedShort())
            objectEffect.deserialize(stream)
            effects.add(objectEffect)
        }
        objectUID = stream.readVarInt()
        quantity = stream.readVarInt()
        objectPrice = stream.readVarLong()
        publicPrice = stream.readVarLong()
    }
}