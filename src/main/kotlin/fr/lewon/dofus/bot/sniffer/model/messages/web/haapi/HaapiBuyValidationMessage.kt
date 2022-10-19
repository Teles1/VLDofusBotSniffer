package fr.lewon.dofus.bot.sniffer.model.messages.web.haapi

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.bot.sniffer.model.types.NetworkType
import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.core.io.stream.BooleanByteWrapper

open class HaapiBuyValidationMessage : HaapiValidationMessage() {
	var amount: Double = 0.0
	var email: String = ""
	override fun deserialize(stream: ByteArrayReader) {
		super.deserialize(stream)
		amount = stream.readVarLong().toDouble()
		email = stream.readUTF()
	}
}
