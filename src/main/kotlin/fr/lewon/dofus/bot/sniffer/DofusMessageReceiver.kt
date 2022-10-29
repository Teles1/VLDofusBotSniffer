package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.core.utils.LockUtils.executeSyncOperation
import fr.lewon.dofus.bot.sniffer.store.EventStore
import org.pcap4j.core.*
import org.pcap4j.core.BpfProgram.BpfCompileMode
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode
import org.pcap4j.packet.IpV4Packet
import org.pcap4j.packet.Packet
import org.pcap4j.packet.TcpPacket
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.locks.ReentrantLock

class DofusMessageReceiver(networkInterfaceName: String) {

    private val lock = ReentrantLock()
    private val handle: PcapHandle
    private val readPacketThread: Thread
    private val treatPacketThread: Thread
    private val connectionByHostPort = HashMap<Host, DofusConnection>()
    private val characterReceiverByConnection = HashMap<DofusConnection, DofusMessageCharacterReceiver>()
    private val ethernetPackets = ArrayBlockingQueue<Packet>(300)
    private var dropped = 0L
    private var droppedByIf = 0L

    init {
        val nif = findActiveDevice(networkInterfaceName)
        handle = PcapHandle.Builder(nif.name)
            .snaplen(65536)
            .promiscuousMode(PromiscuousMode.PROMISCUOUS)
            .timeoutMillis(-1)
            .bufferSize(20 * 1024 * 1024)
            .build()
        updateFilter()
        readPacketThread = Thread { readPackets() }
        treatPacketThread = Thread { treatPackets() }
    }

    private fun readPackets() {
        try {
            while (true) {
                handle.nextPacket?.let {
                    ethernetPackets.add(it)
                }
            }
        } catch (ex: PcapNativeException) {
            println(ex.message)
        } catch (ex: InterruptedException) {
            println(ex.message)
        } catch (ex: NotOpenException) {
            println(ex.message)
        }
    }

    private fun treatPackets() {
        while (true) {
            val stats = handle.stats
            if (stats.numPacketsDropped != dropped) {
                dropped = stats.numPacketsDropped
                println("Dropped : $dropped")
            }
            if (stats.numPacketsDroppedByIf != droppedByIf) {
                droppedByIf = stats.numPacketsDroppedByIf
                println("Dropped by if : $droppedByIf")
            }
            while (ethernetPackets.isNotEmpty()) {
                treatEthernetPacket(ethernetPackets.poll())
            }
            Thread.sleep(50)
        }
    }

    private fun treatEthernetPacket(ethernetPacket: Packet) {
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
                    lock.executeSyncOperation {
                        getCharacterReceiver(dstHost)?.treatReceivedTcpPacket(tcpPacket)
                            ?: getCharacterReceiver(srcHost)?.treatSentTcpPacket(tcpPacket)
                    }
                }
            }
        }
    }

    private fun getCharacterReceiver(host: Host): DofusMessageCharacterReceiver? {
        return lock.executeSyncOperation {
            connectionByHostPort[host]?.let { characterReceiverByConnection[it] }
        }
    }

    fun startListening(connection: DofusConnection, eventStore: EventStore, logger: VldbLogger) {
        lock.executeSyncOperation {
            stopListening(connection.client)
            connectionByHostPort[connection.client] = connection
            characterReceiverByConnection[connection] = DofusMessageCharacterReceiver(connection, eventStore, logger)
            updateFilter()
        }
    }

    fun stopListening(host: Host) {
        lock.executeSyncOperation {
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
        return lock.executeSyncOperation {
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

    fun start() {
        readPacketThread.start()
        treatPacketThread.start()
    }

    fun kill() {
        readPacketThread.interrupt()
        treatPacketThread.interrupt()
        readPacketThread.join()
        treatPacketThread.join()
        handle.close()
    }

    fun isSnifferRunning(): Boolean {
        return readPacketThread.isAlive && treatPacketThread.isAlive
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