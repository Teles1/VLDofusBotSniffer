package fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.types.actor.roleplay.`object`.effect.ObjectEffect

class ObjectItemNotInContainer : Item() {

    var objectGID = -1
    var effects = ArrayList<ObjectEffect>()
    var objectUID = -1
    var quantity = -1

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        objectGID = stream.readVarShort()
        for (i in 0 until stream.readUnsignedShort()) {
            val objectItem = TypeManager.getInstance<ObjectEffect>(stream.readUnsignedShort())
            objectItem.deserialize(stream)
            effects.add(objectItem)
        }
        objectUID = stream.readVarInt()
        quantity = stream.readVarInt()
    }
}