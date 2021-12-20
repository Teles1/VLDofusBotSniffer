package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class DofusMessagePremise(
    val eventName: String,
    val eventId: Int,
    val eventClass: Class<out INetworkMessage>?,
    val stream: ByteArrayReader
)