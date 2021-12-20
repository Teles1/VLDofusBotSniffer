package fr.lewon.dofus.bot.sniffer

data class DofusConnection(
    val hostIp: String,
    val hostPort: String,
    val serverIp: String,
    val serverPort: String,
    val pid: Long
)