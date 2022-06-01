package fr.lewon.dofus.bot.sniffer.model.types.fight.result.entry

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.types.actor.human.social.guild.BasicGuildInformations

class FightResultTaxCollectorListEntry : FightResultFighterListEntry() {

    var level = 0
    val guildInfo = BasicGuildInformations()
    var experienceForGuild = 0

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        level = stream.readUnsignedByte()
        guildInfo.deserialize(stream)
        experienceForGuild = stream.readInt()
    }
}