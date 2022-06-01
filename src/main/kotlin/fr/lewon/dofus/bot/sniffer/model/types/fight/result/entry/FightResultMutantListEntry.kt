package fr.lewon.dofus.bot.sniffer.model.types.fight.result.entry

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

class FightResultMutantListEntry : FightResultFighterListEntry() {

    var level = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        level = stream.readVarShort()
    }
}