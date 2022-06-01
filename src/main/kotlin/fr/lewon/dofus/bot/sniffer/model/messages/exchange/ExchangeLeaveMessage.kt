package fr.lewon.dofus.bot.sniffer.model.messages.exchange

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.interactive.LeaveDialogMessage

class ExchangeLeaveMessage : LeaveDialogMessage() {

    var success = false

    override fun deserialize(stream: ByteArrayReader) {
        super.deserialize(stream)
        success = stream.readBoolean()
    }
}