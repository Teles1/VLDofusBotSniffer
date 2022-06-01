package fr.lewon.dofus.bot.sniffer.model.types.fight.result.entry

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.fight.result.FightResultListEntry

open class FightResultFighterListEntry : FightResultListEntry() {

    var id = 0.0
    var alive = false

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        id = stream.readDouble()
        alive = stream.readBoolean()
    }
}