package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.VLDofusBotCoreUtil
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.sniffer.managers.MessageIdByName
import fr.lewon.dofus.bot.sniffer.store.EventStore
import org.apache.commons.codec.binary.Hex
import org.pcap4j.core.BpfProgram.BpfCompileMode
import org.pcap4j.core.PacketListener
import org.pcap4j.core.PcapHandle
import org.pcap4j.core.PcapNetworkInterface
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode
import org.pcap4j.core.Pcaps
import org.pcap4j.packet.TcpPacket
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

class DofusMessageReceiver(serverIp: String, serverPort: String, hostIp: String, hostPort: String) : Thread() {

    companion object {
        private const val BIT_RIGHT_SHIFT_LEN_PACKET_ID = 2
        private const val BIT_MASK = 3
        private val ID_GENERATOR = AtomicLong(1L)
    }

    val snifferId = ID_GENERATOR.getAndIncrement()

    private val lock = ReentrantLock()
    private val handle: PcapHandle
    private val packetListener: PacketListener
    private val packetTreatmentTimer = Timer()
    private val packetsToTreat = ArrayBlockingQueue<TcpPacket>(50)
    private var staticHeader = 0
    private var splitPacket = false
    private var splitPacketLength = 0
    private var splitPacketId = 0
    private var inputBuffer: ByteArray = ByteArray(0)

    init {
        val nif = findActiveDevice()
        handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, -1)
        handle.setFilter(buildFilter(serverIp, serverPort, hostIp, hostPort), BpfCompileMode.OPTIMIZE)
        packetListener = PacketListener { ethernetPacket ->
            val ipV4Packet = ethernetPacket.payload
            if (ipV4Packet != null) {
                val tcpPacket = ipV4Packet.payload
                if (tcpPacket != null) {
                    if (tcpPacket.payload != null) {
                        packetsToTreat.add(tcpPacket as TcpPacket)
                        packetTreatmentTimer.schedule(buildReceiveTimerTask(), 500L)
                    }
                }
            }
        }
    }

    override fun run() {
        handle.loop(-1, packetListener)
    }

    override fun interrupt() {
        handle.breakLoop()
        handle.close()
        packetTreatmentTimer.cancel()
        packetsToTreat.clear()
    }

    private fun buildFilter(serverIp: String, serverPort: String, hostIp: String, hostPort: String): String {
        return "src host $serverIp and src port $serverPort and dst host $hostIp"
    }

    fun isSnifferRunning(): Boolean {
        return isAlive && handle.isOpen
    }

    private fun buildReceiveTimerTask(): TimerTask {
        return object : TimerTask() {
            override fun run() {
                lock.lock()
                val tcpPacket = packetsToTreat.minByOrNull { it.header.sequenceNumberAsLong }
                    ?: error("No TCP packet to treat")
                println("Parsing packet : ${tcpPacket.header.sequenceNumberAsLong}, new sequence : ${!splitPacket}")
                packetsToTreat.remove(tcpPacket)
                try {
                    receiveData(ByteArrayReader(tcpPacket.payload.rawData))
                } catch (t: Throwable) {
                    println("Couldn't parse packet : ${tcpPacket.header.sequenceNumberAsLong} ; size : ${tcpPacket.payload.rawData.size}")
                    println("Packet currently split : $splitPacket")
                    println("Input buffer size : ${inputBuffer.size}")
                    println(Hex.encodeHexString(tcpPacket.payload.rawData))
                    println(Hex.encodeHexString(tcpPacket.rawData))
                    t.printStackTrace()
                } finally {
                    lock.unlock()
                }
            }
        }
    }

    /** Find the current active pcap network interface.
     * @return The active pcap network interface
     */
    private fun findActiveDevice(): PcapNetworkInterface {
        var currentAddress: InetAddress? = null
        val nis = NetworkInterface.getNetworkInterfaces()
        while (nis.hasMoreElements() && currentAddress == null) {
            val ni = nis.nextElement()
            if (ni.isUp && !ni.isLoopback) {
                val ias = ni.inetAddresses
                while (ias.hasMoreElements() && currentAddress == null) {
                    val ia = ias.nextElement()
                    if (ia.isSiteLocalAddress && !ia.isLoopbackAddress) currentAddress = ia
                }
            }
        }
        currentAddress ?: error("No active address found. Make sure you have an internet connection.")
        return Pcaps.getDevByAddress(currentAddress)
            ?: error("No active device found. Make sure WinPcap or libpcap is installed.")
    }

    fun receiveData(data: ByteArrayReader) {
        if (data.available() > 0) {
            var messageBuilder = lowReceive(data)
            while (messageBuilder != null) {
                process(messageBuilder)
                messageBuilder = lowReceive(data)
            }
        }
    }

    private fun process(messageBuilder: DofusMessageBuilder) {
        messageBuilder.build()?.let { EventStore.addSocketEvent(it, snifferId) }
    }

    private fun lowReceive(src: ByteArrayReader): DofusMessageBuilder? {
        if (!splitPacket) {
            if (src.available() < 2) {
                VldbLogger.error("TOO SMALL TO BE READ : ${Hex.encodeHexString(src.readAllBytes())}")
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
                    return DofusMessageReceiverUtil.parseMessageBuilder(
                        ByteArrayReader(src.readNBytes(messageLength)),
                        messageId
                    )
                }
                staticHeader = -1
                splitPacketLength = messageLength
                splitPacketId = messageId
                splitPacket = true
                inputBuffer = src.readNBytes(src.available())
                return null
            }
            if (MessageIdByName.getName(messageId) == null) {
                error("No message for messageId $messageId / header : $header")
            }
            staticHeader = header
            splitPacketLength = 0
            splitPacketId = messageId
            splitPacket = true
            return null
        }
        if (staticHeader != -1) {
            splitPacketLength = readMessageLength(staticHeader, src)
            staticHeader = -1
        }
        if (src.available() + inputBuffer.size >= splitPacketLength) {
            inputBuffer += src.readNBytes(splitPacketLength - inputBuffer.size)
            val inputBufferReader = ByteArrayReader(inputBuffer)
            val msg = DofusMessageReceiverUtil.parseMessageBuilder(inputBufferReader, splitPacketId)
            splitPacket = false
            inputBuffer = ByteArray(0)
            return msg
        }
        inputBuffer += src.readAllBytes()
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

fun main() {
    DofusMessageReceiverUtil.prepareNetworkManagers()
    VLDofusBotCoreUtil.initAll()

    var str = "750b425245024034000000080076f0"
    while (str.length >= 4) {
        val bar = ByteArrayReader(Hex.decodeHex(str))
        try {
            DofusMessageReceiver("1.1.1.1", "12345", "2.2.2.2", "54321").receiveData(bar)
            println("OK FOR $str")
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        str = str.substring(2)
    }
}