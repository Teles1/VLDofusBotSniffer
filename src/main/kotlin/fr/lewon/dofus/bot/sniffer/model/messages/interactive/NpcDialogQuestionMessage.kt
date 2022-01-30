package fr.lewon.dofus.bot.sniffer.model.messages.interactive

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage

class NpcDialogQuestionMessage : INetworkMessage {

    var messageId = 0
    val dialogParams = ArrayList<String>()
    val visibleReplies = ArrayList<Int>()

    override fun deserialize(stream: ByteArrayReader) {
        messageId = stream.readVarInt()
        for (i in 0 until stream.readUnsignedShort()) {
            dialogParams.add(stream.readUTF())
        }
        for (i in 0 until stream.readUnsignedShort()) {
            visibleReplies.add(stream.readVarInt())
        }
    }
}