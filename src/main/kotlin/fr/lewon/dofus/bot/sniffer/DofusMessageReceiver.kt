package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.sniffer.store.EventStore
import org.pcap4j.core.*
import org.pcap4j.core.BpfProgram.BpfCompileMode
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode
import org.pcap4j.packet.TcpPacket
import java.util.concurrent.locks.ReentrantLock

class DofusMessageReceiver(networkInterfaceName: String) : Thread() {

    private val lock = ReentrantLock(true)
    private val handle: PcapHandle
    private val packetListener: PacketListener
    private val dofusConnectionByHostPort = HashMap<String, DofusConnection>()
    private val characterReceiverByConnection = HashMap<DofusConnection, DofusMessageCharacterReceiver>()

    init {
        val nif = findActiveDevice(networkInterfaceName)
        handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, -1)
        updateFilter()
        packetListener = PacketListener { ethernetPacket ->
            val ipV4Packet = ethernetPacket.payload
            if (ipV4Packet != null) {
                val tcpPacket = ipV4Packet.payload
                if (tcpPacket != null) {
                    if (tcpPacket.payload != null) {
                        getCharacterReceiver(tcpPacket as TcpPacket).receiveTcpPacket(tcpPacket)
                    }
                }
            }
        }
    }

    fun startListening(dofusConnection: DofusConnection, eventStore: EventStore, logger: VldbLogger) {
        try {
            lock.lockInterruptibly()
            stopListening(dofusConnection.hostPort)
            dofusConnectionByHostPort[dofusConnection.hostPort] = dofusConnection
            val hostState = HostState(dofusConnection, eventStore, logger)
            characterReceiverByConnection[dofusConnection] = DofusMessageCharacterReceiver(hostState)
            updateFilter()
        } finally {
            lock.unlock()
        }
    }

    fun stopListening(hostPort: String) {
        try {
            lock.lockInterruptibly()
            val connection = dofusConnectionByHostPort.remove(hostPort)
            characterReceiverByConnection.remove(connection)?.stopAll()
            updateFilter()
        } finally {
            lock.unlock()
        }
    }

    private fun updateFilter() {
        val filter = if (dofusConnectionByHostPort.isEmpty()) {
            "src host 255.255.255.255 and dst host 255.255.255.255"
        } else {
            buildFilter()
        }
        handle.setFilter(filter, BpfCompileMode.OPTIMIZE)
    }

    private fun buildFilter(): String {
        val srcHostPart = dofusConnectionByHostPort.values.map { it.serverIp }.joinToString(" or ") { "src host $it" }
        val srcPortPart = dofusConnectionByHostPort.values.map { it.serverPort }.joinToString(" or ") { "src port $it" }
        val dstHostPart = dofusConnectionByHostPort.values.map { it.hostIp }.joinToString(" or ") { "dst host $it" }
        val dstPortPart = dofusConnectionByHostPort.values.map { it.hostPort }.joinToString(" or ") { "dst port $it" }
        return "($srcHostPart) and ($srcPortPart) and ($dstHostPart) and ($dstPortPart)"
    }

    override fun run() {
        try {
            handle.loop(-1, packetListener)
        } catch (ex: PcapNativeException) {
        } catch (ex: InterruptedException) {
        } catch (ex: NotOpenException) {
        }
    }

    override fun interrupt() {
        handle.breakLoop()
        handle.close()
    }

    fun isSnifferRunning(): Boolean {
        return isAlive && handle.isOpen
    }

    private fun getCharacterReceiver(tcpPacket: TcpPacket): DofusMessageCharacterReceiver {
        try {
            lock.lockInterruptibly()
            val hostPortStr = tcpPacket.header.dstPort.valueAsString()
            val dofusConnection = dofusConnectionByHostPort[hostPortStr]
                ?: error("Unknown connection for port : $hostPortStr")
            return characterReceiverByConnection[dofusConnection]
                ?: error("Unknown character receiver for port : ${dofusConnection.hostPort}")
        } finally {
            lock.unlock()
        }
    }

    /** Find the current active pcap network interface.
     * @return The active pcap network interface
     */
    private fun findActiveDevice(networkInterfaceName: String): PcapNetworkInterface {
        val inetAddress = DofusMessageReceiverUtil.findInetAddress(networkInterfaceName)
            ?: error("No active address found. Make sure you have an internet connection.")
        return Pcaps.getDevByAddress(inetAddress)
            ?: error("No active device found. Make sure WinPcap or libpcap is installed.")
    }

}