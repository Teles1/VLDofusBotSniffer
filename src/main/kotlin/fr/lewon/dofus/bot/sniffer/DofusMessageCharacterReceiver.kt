package fr.lewon.dofus.bot.sniffer

import com.fasterxml.jackson.databind.ObjectMapper
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.managers.MessageIdByName
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import org.apache.commons.codec.binary.Hex
import org.pcap4j.packet.TcpPacket

class DofusMessageCharacterReceiver(private val hostState: HostState) {

    companion object {
        private const val BIT_RIGHT_SHIFT_LEN_PACKET_ID = 2
        private const val BIT_MASK = 3
    }

    private val objectMapper = ObjectMapper()

    fun receiveTcpPacket(tcpPacket: TcpPacket) {
        val rawData = hostState.leftoverBuffer + tcpPacket.payload.rawData
        hostState.leftoverBuffer = ByteArray(0)
        try {
            receiveData(hostState, ByteArrayReader(rawData))
        } catch (t: Throwable) {
            t.printStackTrace()
            val rawDataStr = Hex.encodeHexString(rawData)
            println("Couldn't receive data : $rawDataStr")
            hostState.logger.log("ERROR : Couldn't receive data : $rawDataStr")
        }
    }

    private fun receiveData(hostState: HostState, data: ByteArrayReader) {
        if (data.available() > 0) {
            var messagePremise = lowReceive(hostState, data)
            while (messagePremise != null) {
                process(messagePremise, hostState)
                messagePremise = lowReceive(hostState, data)
            }
        }
    }

    private fun process(messagePremise: DofusMessagePremise, hostState: HostState) {
        val premiseStr = "${messagePremise.eventName}:${messagePremise.eventId}"
        val message = deserializeMessage(messagePremise)
        if (message != null) {
            addMessageToStore(message, hostState)
            val messageStr = objectMapper.writeValueAsString(message)
            hostState.logger.log(premiseStr, description = messageStr)
        } else {
            hostState.logger.log("[UNTREATED] : $premiseStr")
        }
    }

    private fun deserializeMessage(messagePremise: DofusMessagePremise): INetworkMessage? {
        return try {
            messagePremise.eventClass?.getConstructor()?.newInstance()
                ?.also { it.deserialize(messagePremise.stream) }
        } catch (t: Throwable) {
            println("ERROR deserializing message ${messagePremise.eventClass} :")
            t.printStackTrace()
            null
        }
    }

    private fun addMessageToStore(message: INetworkMessage, hostState: HostState) {
        try {
            hostState.eventStore.addSocketEvent(message, hostState.connection)
        } catch (t: Throwable) {
            println("ERROR adding event to store :")
            t.printStackTrace()
        }
    }

    private fun lowReceive(hostState: HostState, src: ByteArrayReader): DofusMessagePremise? {
        if (!hostState.splitPacket) {
            if (src.available() < 2) {
                hostState.leftoverBuffer = src.readAllBytes()
                return null
            }
            val header = src.readUnsignedShort()
            val messageId = header shr BIT_RIGHT_SHIFT_LEN_PACKET_ID
            if (src.available() >= (header and BIT_MASK)) {
                val messageLength = readMessageLength(header, src)
                if (MessageIdByName.getName(messageId) == null) {
                    error("No message for messageId $messageId / header : $header / length : $messageLength")
                }
                if (src.available() >= messageLength) {
                    return DofusMessageReceiverUtil.parseMessagePremise(
                        ByteArrayReader(src.readNBytes(messageLength)),
                        messageId
                    )
                }
                hostState.staticHeader = -1
                hostState.splitPacketLength = messageLength
                hostState.splitPacketId = messageId
                hostState.splitPacket = true
                hostState.inputBuffer = src.readNBytes(src.available())
                return null
            }
            if (MessageIdByName.getName(messageId) == null) {
                error("No message for messageId $messageId / header : $header")
            }
            hostState.staticHeader = header
            hostState.splitPacketLength = 0
            hostState.splitPacketId = messageId
            hostState.splitPacket = true
            return null
        }
        if (hostState.staticHeader != -1) {
            hostState.splitPacketLength = readMessageLength(hostState.staticHeader, src)
            hostState.staticHeader = -1
        }
        if (src.available() + hostState.inputBuffer.size >= hostState.splitPacketLength) {
            hostState.inputBuffer += src.readNBytes(hostState.splitPacketLength - hostState.inputBuffer.size)
            val inputBufferReader = ByteArrayReader(hostState.inputBuffer)
            val msg = DofusMessageReceiverUtil.parseMessagePremise(inputBufferReader, hostState.splitPacketId)
            hostState.splitPacket = false
            hostState.inputBuffer = ByteArray(0)
            return msg
        }
        hostState.inputBuffer += src.readAllBytes()
        return null
    }

    private fun readMessageLength(staticHeader: Int, src: ByteArrayReader): Int {
        return when (staticHeader and BIT_MASK) {
            0 -> 0
            1 -> src.readUnsignedByte()
            2 -> src.readUnsignedShort()
            3 -> ((src.readUnsignedByte() and 255) shl 16) + ((src.readUnsignedByte() and 255) shl 8) + (src.readUnsignedByte() and 255)
            else -> error("Invalid length")
        }
    }

}