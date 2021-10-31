package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.sniffer.managers.MessageIdByName
import fr.lewon.dofus.bot.sniffer.store.EventStore
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

object DofusMessageReceiver {

    private const val BIT_RIGHT_SHIFT_LEN_PACKET_ID = 2
    private const val BIT_MASK = 3

    private lateinit var handle: PcapHandle
    private lateinit var packetListener: PacketListener
    private lateinit var packetTreatmentTimer: Timer
    private val packetsToTreat = ArrayBlockingQueue<TcpPacket>(50)
    private var staticHeader = 0
    private var splitPacket = false
    private var splitPacketLength = 0
    private var splitPacketId = 0
    private var inputBuffer: ByteArray = ByteArray(0)
    private var currentThread: Thread = buildThread()

    fun killAndStartThread() {
        val nif = findActiveDevice()
        handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, -1)
        val serverIp = DofusMessageReceiverUtil.findServerIp()
        handle.setFilter("src $serverIp", BpfCompileMode.OPTIMIZE)
        packetListener = PacketListener { ethernetPacket ->
            val ipV4Packet = ethernetPacket.payload
            if (ipV4Packet != null) {
                val tcpPacket = ipV4Packet.payload
                if (tcpPacket != null) {
                    if (tcpPacket.payload != null) {
                        packetsToTreat.add(tcpPacket as TcpPacket)
                        packetTreatmentTimer.schedule(buildReceiveTimerTask(), 150L)
                    }
                }
            }
        }
        if (isThreadAlive()) {
            currentThread.interrupt()
        }
        packetsToTreat.clear()
        packetTreatmentTimer = Timer()
        currentThread = buildThread()
        currentThread.start()
    }

    private fun buildReceiveTimerTask(): TimerTask {
        return object : TimerTask() {
            override fun run() {
                val tcpPacket = packetsToTreat.minByOrNull { it.header.sequenceNumberAsLong }
                    ?: error("No TCP packet to treat")
                packetsToTreat.remove(tcpPacket)
                receiveData(ByteArrayReader(tcpPacket.payload.rawData))
            }
        }
    }

    private fun buildThread(): Thread {
        return object : Thread() {
            override fun run() {
                handle.loop(-1, packetListener)
            }

            override fun interrupt() {
                handle.breakLoop()
                handle.close()
                packetTreatmentTimer.cancel()
                packetTreatmentTimer.purge()
            }
        }
    }

    fun isThreadAlive(): Boolean {
        return currentThread.isAlive
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
        if (currentAddress == null) error("No active address found. Make sure you have an internet connection.")
        return Pcaps.getDevByAddress(currentAddress)
            ?: error("No active device found. Make sure WinPcap or libpcap is installed.")
    }

    private fun receiveData(data: ByteArrayReader) {
        VldbLogger.trace(" ------- ")
        if (data.available() > 0) {
            var messageBuilder = lowReceive(data)
            while (messageBuilder != null) {
                process(messageBuilder)
                messageBuilder = lowReceive(data)
            }
        }
    }

    private fun process(messageBuilder: DofusMessageBuilder) {
        messageBuilder.build()?.let { EventStore.addSocketEvent(it) }
    }

    private fun lowReceive(src: ByteArrayReader): DofusMessageBuilder? {
        VldbLogger.trace(" -- low receive -- ")
        if (!splitPacket) {
            if (src.available() < 2) {
                VldbLogger.trace("Not enough data to read the header, byte available : " + src.available() + " (needed : 2)")
                return null
            }
            val header = src.readUnsignedShort()
            val messageId = header shr BIT_RIGHT_SHIFT_LEN_PACKET_ID
            if (src.available() >= (header and BIT_MASK)) {
                val messageLength = readMessageLength(header, src)
                VldbLogger.trace("=> header = $header")
                VldbLogger.trace("=> messageId = $messageId")
                VldbLogger.trace("=> sh and bitmask = ${header and BIT_MASK}")
                VldbLogger.trace("=> message length = $messageLength")
                if (MessageIdByName.getName(messageId) == null) {
                    VldbLogger.trace("=> No message for messageId $messageId")
                    return null
                }
                if (src.available() >= messageLength) {
                    VldbLogger.trace("Full parsing done")
                    val msg = DofusMessageReceiverUtil.parseMessageBuilder(
                        ByteArrayReader(src.readNBytes(messageLength)),
                        messageId
                    )
                    VldbLogger.trace("=> Bytes left : ${src.available()}")
                    return msg
                }
                VldbLogger.trace("Not enough data to read msg [$messageId] content, byte available : " + src.available() + " (needed : " + messageLength + ")")
                staticHeader = -1
                splitPacketLength = messageLength
                splitPacketId = messageId
                splitPacket = true
                inputBuffer = src.readNBytes(src.available())
                return null
            }
            VldbLogger.trace("Not enough data to read message ID, byte available : " + src.available() + " (needed : " + (staticHeader and BIT_MASK).toString() + ")")
            VldbLogger.trace("=> header = $header")
            VldbLogger.trace("=> messageId = $messageId")
            VldbLogger.trace("=> sh and bitmask = ${header and BIT_MASK}")
            VldbLogger.trace("=> message length = 0")
            if (MessageIdByName.getName(messageId) == null) {
                VldbLogger.trace("=> No message for messageId $messageId")
                return null
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
            VldbLogger.trace("Full parsing done")
            val inputBufferReader = ByteArrayReader(inputBuffer)
            val msg = DofusMessageReceiverUtil.parseMessageBuilder(inputBufferReader, splitPacketId)
            VldbLogger.trace("=> Bytes left : ${src.available()}")
            splitPacket = false
            inputBuffer = ByteArray(0)
            return msg
        }
        VldbLogger.trace("=> Saved to buffer : ${src.available()}")
        inputBuffer += src.readAllBytes()
        VldbLogger.trace("=> Total buffer size : ${inputBuffer.size}")
        VldbLogger.trace("=> Needed size : $splitPacketLength")
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