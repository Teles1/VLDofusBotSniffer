package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.core.utils.LockUtils
import fr.lewon.dofus.bot.sniffer.store.EventStore
import org.pcap4j.core.*
import org.pcap4j.core.BpfProgram.BpfCompileMode
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode
import org.pcap4j.packet.IpV4Packet
import org.pcap4j.packet.Packet
import org.pcap4j.packet.TcpPacket
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.locks.ReentrantLock

class DofusMessageReceiver(networkInterfaceName: String) : Thread() {

    private val lock = ReentrantLock()
    private val handle: PcapHandle
    private val packetListener: PacketListener
    private val connectionByHostPort = HashMap<Host, DofusConnection>()
    private val characterReceiverByConnection = HashMap<DofusConnection, DofusMessageCharacterReceiver>()
    private val ethernetPackets = ArrayBlockingQueue<Packet>(300)
    private val timer = Timer()

    init {
        val nif = findActiveDevice(networkInterfaceName)
        handle = nif.openLive(65536000, PromiscuousMode.PROMISCUOUS, -1)
        updateFilter()
        packetListener = PacketListener {
            ethernetPackets.add(it)
            timer.schedule(buildTreatEthernetPacketTimerTask(), 0)
        }
    }

    private fun buildTreatEthernetPacketTimerTask() = object : TimerTask() {
        override fun run() {
            treatEthernetPacket()
        }
    }

    private fun treatEthernetPacket() {
        val ethernetPacket = ethernetPackets.poll()
        val ipV4Packet = ethernetPacket.payload
        if (ipV4Packet != null) {
            val tcpPacket = ipV4Packet.payload
            if (tcpPacket != null) {
                if (tcpPacket.payload != null) {
                    val srcPort = (tcpPacket as TcpPacket).header.srcPort.valueAsString()
                    val srcIp = (ipV4Packet as IpV4Packet).header.srcAddr.hostAddress
                    val dstPort = tcpPacket.header.dstPort.valueAsString()
                    val dstIp = ipV4Packet.header.dstAddr.hostAddress
                    val srcHost = Host(srcIp, srcPort)
                    val dstHost = Host(dstIp, dstPort)
                    LockUtils.executeSyncOperation(lock) {
                        getCharacterReceiver(dstHost)?.treatReceivedTcpPacket(tcpPacket)
                            ?: getCharacterReceiver(srcHost)?.treatSentTcpPacket(tcpPacket)
                    }
                }
            }
        }
    }

    private fun getCharacterReceiver(host: Host): DofusMessageCharacterReceiver? {
        return LockUtils.executeSyncOperation(lock) {
            connectionByHostPort[host]?.let { characterReceiverByConnection[it] }
        }
    }

    fun startListening(connection: DofusConnection, eventStore: EventStore, logger: VldbLogger) {
        LockUtils.executeSyncOperation(lock) {
            stopListening(connection.client)
            connectionByHostPort[connection.client] = connection
            characterReceiverByConnection[connection] = DofusMessageCharacterReceiver(connection, eventStore, logger)
            updateFilter()
        }
    }

    fun stopListening(host: Host) {
        LockUtils.executeSyncOperation(lock) {
            val connection = connectionByHostPort.remove(host)
            characterReceiverByConnection.remove(connection)
            updateFilter()
        }
    }

    private fun updateFilter() {
        val filter = if (connectionByHostPort.isEmpty()) {
            "src host 255.255.255.255 and dst host 255.255.255.255"
        } else {
            buildFilter()
        }
        handle.setFilter(filter, BpfCompileMode.OPTIMIZE)
    }

    private fun buildFilter(): String {
        return LockUtils.executeSyncOperation(lock) {
            val connections = connectionByHostPort.values
            val clientToServerFilters = connections.map {
                "src host ${it.client.ip} and src port ${it.client.port} and dst host ${it.server.ip} and dst port ${it.server.port}"
            }
            val serverToClientFilters = connections.map {
                "src host ${it.server.ip} and src port ${it.server.port} and dst host ${it.client.ip} and dst port ${it.client.port}"
            }
            clientToServerFilters.plus(serverToClientFilters).joinToString(" or ") { "($it)" }
        }
    }

    override fun run() {
        try {
            handle.loop(-1, packetListener)
        } catch (ex: PcapNativeException) {
            println(ex.message)
        } catch (ex: InterruptedException) {
            println(ex.message)
        } catch (ex: NotOpenException) {
            println(ex.message)
        }
    }

    override fun interrupt() {
        handle.breakLoop()
        handle.close()
        timer.cancel()
    }

    fun isSnifferRunning(): Boolean {
        return isAlive && handle.isOpen
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