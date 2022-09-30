package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.sniffer.store.EventStore

class HostState(
    val connection: DofusConnection,
    val eventStore: EventStore,
    val logger: VldbLogger,
)