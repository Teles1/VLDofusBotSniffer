package fr.lewon.dofus.bot.sniffer.model.types.fight.summon

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.sniffer.model.TypeManager
import fr.lewon.dofus.bot.sniffer.model.types.actor.entity.EntityLook
import fr.lewon.dofus.bot.sniffer.model.types.fight.GameContextBasicSpawnInformation
import fr.lewon.dofus.bot.sniffer.model.types.fight.charac.GameFightCharacteristics
import fr.lewon.dofus.bot.sniffer.model.types.fight.summon.spawn.SpawnInformation

class GameContextSummonsInformation : INetworkType {

    lateinit var spawnInformation: SpawnInformation
    var wave = 0
    val look = EntityLook()
    lateinit var stats: GameFightCharacteristics
    val summons = ArrayList<GameContextBasicSpawnInformation>()

    override fun deserialize(stream: ByteArrayReader) {
        spawnInformation = TypeManager.getInstance(stream.readUnsignedShort())
        spawnInformation.deserialize(stream)
        wave = stream.readUnsignedByte()
        look.deserialize(stream)
        stats = TypeManager.getInstance(stream.readUnsignedShort())
        stats.deserialize(stream)
        for (i in 0 until stream.readUnsignedShort()) {
            val summon = TypeManager.getInstance<GameContextBasicSpawnInformation>(stream.readUnsignedShort())
            summon.deserialize(stream)
            summons.add(summon)
        }
    }
}