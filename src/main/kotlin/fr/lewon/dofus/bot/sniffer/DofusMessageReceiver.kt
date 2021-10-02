package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.sniffer.managers.MessageIdByName
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.bot.sniffer.store.EventStore
import fr.lewon.dofus.bot.util.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.util.logs.VldbLogger
import org.pcap4j.core.BpfProgram.BpfCompileMode
import org.pcap4j.core.PacketListener
import org.pcap4j.core.PcapHandle
import org.pcap4j.core.PcapNetworkInterface
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode
import org.pcap4j.core.Pcaps
import java.net.InetAddress
import java.net.NetworkInterface

object DofusMessageReceiver {

    private const val BIT_RIGHT_SHIFT_LEN_PACKET_ID = 2
    private const val BIT_MASK = 3

    private lateinit var handle: PcapHandle
    private lateinit var packetListener: PacketListener
    private var staticHeader = 0
    private var splittedPacket = false
    private var splittedPacketLength = 0
    private var splittedPacketId = 0
    private var inputBuffer: ByteArray = ByteArray(0)
    private var currentThread: Thread = buildThread()

    fun restartThread() {
        val nif = findActiveDevice()
        handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, -1)
        val serverIp = DofusMessageReceiverUtil.findServerIp()
        handle.setFilter("src $serverIp", BpfCompileMode.OPTIMIZE)
        packetListener = PacketListener { packet ->
            val ethernetPacket = packet.payload
            if (ethernetPacket != null) {
                val tcpPacket = ethernetPacket.payload
                if (tcpPacket != null) {
                    val dofusPacket = tcpPacket.payload
                    if (dofusPacket != null) {
                        receiveData(ByteArrayReader(dofusPacket.rawData))
                    }
                }
            }
        }
        if (isThreadAlive()) {
            currentThread.interrupt()
        }
        currentThread = buildThread()
        currentThread.start()
    }

    private fun buildThread(): Thread {
        return object : Thread() {
            override fun run() {
                handle.loop(-1, packetListener)
            }

            override fun interrupt() {
                handle.breakLoop()
                handle.close()
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
            var messageReceiver = lowReceive(data)
            while (messageReceiver != null) {
                messageReceiver.build()?.let { process(it) }
                messageReceiver = lowReceive(data)
            }
        }
    }

    private fun process(msg: INetworkMessage) {
        EventStore.addSocketEvent(msg)
    }

    private fun lowReceive(src: ByteArrayReader): DofusMessageBuilder? {
        VldbLogger.trace(" -- low receive -- ")
        if (!splittedPacket) {
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
                splittedPacketLength = messageLength
                splittedPacketId = messageId
                splittedPacket = true
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
            splittedPacketLength = 0
            splittedPacketId = messageId
            splittedPacket = true
            return null
        }
        if (staticHeader != -1) {
            splittedPacketLength = readMessageLength(staticHeader, src)
            staticHeader = -1
        }
        if (src.available() + inputBuffer.size >= splittedPacketLength) {
            inputBuffer += src.readNBytes(splittedPacketLength - inputBuffer.size)
            VldbLogger.trace("Full parsing done")
            val inputBufferReader = ByteArrayReader(inputBuffer)
            val msg = DofusMessageReceiverUtil.parseMessageBuilder(inputBufferReader, splittedPacketId)
            VldbLogger.trace("=> Bytes left : ${src.available()} / ${inputBufferReader.available()}")
            splittedPacket = false
            inputBuffer = ByteArray(0)
            return msg
        }
        inputBuffer += src.readNBytes(src.available())
        return null
    }

    private fun readMessageLength(staticHeader: Int, src: ByteArrayReader): Int {
        return when (staticHeader and BIT_MASK) {
            0 -> 0
            1 -> src.readUnsignedByte()
            2 -> src.readUnsignedShort()
            3 -> ((src.readByte().toInt() and 255) shl 16) + ((src.readByte().toInt() and 255) shl 8) + (src.readByte()
                .toInt() and 255)
            else -> error("Invalid length")
        }

    }

}