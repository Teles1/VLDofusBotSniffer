package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.logs.VldbLogger

class DofusMessageBuilder(
    private val eventName: String,
    private val eventId: Int,
    private val eventClass: Class<out INetworkMessage>?,
    private val stream: ByteArrayReader
) {

    fun build(): INetworkMessage? {
        val untreatedStr = if (eventClass == null) "[UNTREATED] " else ""
        VldbLogger.debug("${untreatedStr}Message received : [$eventName:$eventId]")
        return eventClass?.getConstructor()?.newInstance()
            ?.also { it.deserialize(stream) }
    }

}