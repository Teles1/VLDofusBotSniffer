package fr.lewon.dofus.bot.sniffer.model.messages.chat

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class TextInformationMessage : INetworkMessage {

    var msgType = 0
    var msgId = 0
    var parameters = ArrayList<String>()

    override fun deserialize(stream: ByteArrayReader) {
        msgType = stream.readUnsignedByte()
        msgId = stream.readVarShort()
        for (i in 0 until stream.readUnsignedShort()) {
            parameters.add(stream.readUTF())
        }
    }
}