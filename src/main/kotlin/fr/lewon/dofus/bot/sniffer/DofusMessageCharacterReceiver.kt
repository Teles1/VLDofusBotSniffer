package fr.lewon.dofus.bot.sniffer

import com.fasterxml.jackson.databind.ObjectMapper
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.exceptions.AddToStoreFailedException
import fr.lewon.dofus.bot.sniffer.exceptions.IncompleteMessageException
import fr.lewon.dofus.bot.sniffer.exceptions.MessageIdNotFoundException
import fr.lewon.dofus.bot.sniffer.exceptions.ParseFailedException
import fr.lewon.dofus.bot.sniffer.managers.MessageIdByName
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import org.pcap4j.packet.TcpPacket

class DofusMessageCharacterReceiver(private val hostState: HostState) {

    companion object {
        private const val BIT_RIGHT_SHIFT_LEN_PACKET_ID = 2
        private const val BIT_MASK = 3
    }

    private val objectMapper = ObjectMapper()
    private val packets = ArrayList<TcpPacket>()

    fun receiveTcpPacket(tcpPacket: TcpPacket) {
        packets.add(tcpPacket)
        readPackets()
    }

    private fun readPackets() {
        try {
            handlePackets()
        } catch (e: IncompleteMessageException) {
            // Nothing
        } catch (e: Exception) {
            println("Port ${hostState.connection.hostPort} : Couldn't read message - ${e.message}")
            if (packets.size > 6) {
                packets.remove(getSortedPackets().first())
                handlePackets()
            }
            e.printStackTrace()
        }
    }

    private fun getSortedPackets(): List<TcpPacket> {
        return packets.sortedBy { it.header.sequenceNumberAsLong }
    }

    private fun handlePackets() {
        val rawData = getSortedPackets().flatMap { it.payload.rawData.toList() }.toByteArray()
        val bar = ByteArrayReader(rawData)
        val premises = receiveData(bar)
        if (bar.available() == 0) {
            for (premise in premises) {
                process(premise, hostState)
            }
            packets.clear()
        }
    }

    private fun receiveData(data: ByteArrayReader): List<DofusMessagePremise> {
        val premises = ArrayList<DofusMessagePremise>()
        if (data.available() > 0) {
            var messagePremise = lowReceive(data)
            while (messagePremise != null) {
                premises.add(messagePremise)
                messagePremise = lowReceive(data)
            }
        }
        return premises
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
            messagePremise.eventClass?.getConstructor()?.newInstance()?.also { it.deserialize(messagePremise.stream) }
        } catch (t: Throwable) {
            throw ParseFailedException(messagePremise.eventName, messagePremise.eventId, t)
        }
    }

    private fun addMessageToStore(message: INetworkMessage, hostState: HostState) {
        try {
            hostState.eventStore.addSocketEvent(message, hostState.connection)
        } catch (t: Throwable) {
            throw AddToStoreFailedException(message::class.java.toString(), t)
        }
    }

    private fun lowReceive(src: ByteArrayReader): DofusMessagePremise? {
        if (src.available() < 2) {
            return null
        }
        val header = src.readUnsignedShort()
        val messageId = header shr BIT_RIGHT_SHIFT_LEN_PACKET_ID
        if (src.available() >= (header and BIT_MASK)) {
            val messageLength = readMessageLength(header, src)
            if (MessageIdByName.getName(messageId) == null) {
                throw MessageIdNotFoundException(messageId)
            }
            if (src.available() >= messageLength) {
                val stream = ByteArrayReader(src.readNBytes(messageLength))
                return DofusMessageReceiverUtil.parseMessagePremise(stream, messageId)
            }
        }
        throw IncompleteMessageException()
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