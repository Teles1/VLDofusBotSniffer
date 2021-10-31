package fr.lewon.dofus.bot.sniffer.model.types.fight.charac

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.INetworkType

open class GameFightMinimalStats : INetworkType {

    var lifePoints = 0
    var maxLifePoints = 0
    var baseMaxLifePoints = 0
    var permanentDamagePercent = 0
    var shieldPoints = 0
    var actionPoints = 0
    var maxActionPoints = 0
    var movementPoints = 0
    var maxMovementPoints = 0
    var summoner = 0.0
    var summoned = false
    var neutralElementResistPercent = 0
    var earthElementResistPercent = 0
    var waterElementResistPercent = 0
    var airElementResistPercent = 0
    var fireElementResistPercent = 0
    var neutralElementReduction = 0
    var earthElementReduction = 0
    var waterElementReduction = 0
    var airElementReduction = 0
    var fireElementReduction = 0
    var criticalDamageFixedResist = 0
    var pushDamageFixedResist = 0
    var pvpNeutralElementResistPercent = 0
    var pvpEarthElementResistPercent = 0
    var pvpWaterElementResistPercent = 0
    var pvpAirElementResistPercent = 0
    var pvpFireElementResistPercent = 0
    var pvpNeutralElementReduction = 0
    var pvpEarthElementReduction = 0
    var pvpWaterElementReduction = 0
    var pvpAirElementReduction = 0
    var pvpFireElementReduction = 0
    var dodgePALostProbability = 0
    var dodgePMLostProbability = 0
    var tackleBlock = 0
    var tackleEvade = 0
    var fixedDamageReflection = 0
    var invisibilityState = 0
    var meleeDamageReceivedPercent = 0
    var rangedDamageReceivedPercent = 0
    var weaponDamageReceivedPercent = 0
    var spellDamageReceivedPercent = 0

    override fun deserialize(stream: ByteArrayReader) {
        lifePoints = stream.readVarInt()
        maxLifePoints = stream.readVarInt()
        baseMaxLifePoints = stream.readVarInt()
        permanentDamagePercent = stream.readVarInt()
        shieldPoints = stream.readVarInt()
        actionPoints = stream.readVarShort()
        maxActionPoints = stream.readVarShort()
        movementPoints = stream.readVarShort()
        maxMovementPoints = stream.readVarShort()
        summoner = stream.readDouble()
        summoned = stream.readBoolean()
        neutralElementResistPercent = stream.readVarShort()
        earthElementResistPercent = stream.readVarShort()
        waterElementResistPercent = stream.readVarShort()
        airElementResistPercent = stream.readVarShort()
        fireElementResistPercent = stream.readVarShort()
        neutralElementReduction = stream.readVarShort()
        earthElementReduction = stream.readVarShort()
        waterElementReduction = stream.readVarShort()
        airElementReduction = stream.readVarShort()
        fireElementReduction = stream.readVarShort()
        criticalDamageFixedResist = stream.readVarShort()
        pushDamageFixedResist = stream.readVarShort()
        pvpNeutralElementResistPercent = stream.readVarShort()
        pvpEarthElementResistPercent = stream.readVarShort()
        pvpWaterElementResistPercent = stream.readVarShort()
        pvpAirElementResistPercent = stream.readVarShort()
        pvpFireElementResistPercent = stream.readVarShort()
        pvpNeutralElementReduction = stream.readVarShort()
        pvpEarthElementReduction = stream.readVarShort()
        pvpWaterElementReduction = stream.readVarShort()
        pvpAirElementReduction = stream.readVarShort()
        pvpFireElementReduction = stream.readVarShort()
        dodgePALostProbability = stream.readVarShort()
        dodgePMLostProbability = stream.readVarShort()
        tackleBlock = stream.readVarShort()
        tackleEvade = stream.readVarShort()
        fixedDamageReflection = stream.readVarShort()
        invisibilityState = stream.readUnsignedByte()
        meleeDamageReceivedPercent = stream.readVarShort()
        rangedDamageReceivedPercent = stream.readVarShort()
        weaponDamageReceivedPercent = stream.readVarShort()
        spellDamageReceivedPercent = stream.readVarShort()
    }

}