package fr.lewon.dofus.bot.sniffer

data class DofusConnection(
    val client: Host,
    val server: Host,
    val pid: Long
)