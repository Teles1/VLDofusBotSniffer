package fr.lewon.dofus.bot.sniffer.model.types.game.presets

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class SpellsPreset : Preset() {
	var spells: ArrayList<SpellForPreset> = ArrayList()
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		spells = ArrayList()
		for (i in 0 until stream.readUnsignedShort()) {
			val item = SpellForPreset()
			item.deserialize(stream)
			spells.add(item)
		}
	}
}
