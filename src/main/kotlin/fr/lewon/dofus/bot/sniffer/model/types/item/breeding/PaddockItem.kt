package fr.lewon.dofus.bot.sniffer.model.types.item.breeding

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.item.ItemDurability
import fr.lewon.dofus.bot.sniffer.model.types.item.ObjectItemInRolePlay

class PaddockItem : ObjectItemInRolePlay() {

    var durability = ItemDurability()

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        durability.deserialize(stream)
    }
}