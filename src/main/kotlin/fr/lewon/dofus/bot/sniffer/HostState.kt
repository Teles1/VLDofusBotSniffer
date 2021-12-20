package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.sniffer.store.EventStore

class HostState(
    val connection: DofusConnection,
    val eventStore: EventStore,
    val logger: VldbLogger,
    var staticHeader: Int = 0,
    var splitPacket: Boolean = false,
    var splitPacketLength: Int = 0,
    var splitPacketId: Int = 0,
    var inputBuffer: ByteArray = ByteArray(0),
    var leftoverBuffer: ByteArray = ByteArray(0)
)