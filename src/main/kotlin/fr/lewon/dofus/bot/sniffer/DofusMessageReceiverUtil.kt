package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.VldbProtocolUpdater
import fr.lewon.dofus.bot.core.io.gamefiles.VldbFilesUtil
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.logs.VldbLogger
import fr.lewon.dofus.bot.sniffer.managers.MessageIdByName
import fr.lewon.dofus.bot.sniffer.managers.TypeIdByName
import fr.lewon.dofus.bot.sniffer.model.messages.INetworkMessage
import fr.lewon.dofus.export.builder.VldbIdByNameExportPackTaskBuilder
import org.reflections.Reflections
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object DofusMessageReceiverUtil {

    private lateinit var messagesById: Map<Int, Class<out INetworkMessage>>

    fun parseMessageBuilder(stream: ByteArrayReader, messageId: Int): DofusMessageBuilder? {
        val messageType = messagesById[messageId]
        val messageName = MessageIdByName.getName(messageId) ?: return null
        return DofusMessageBuilder(messageName, messageId, messageType, stream)
    }

    fun prepareNetworkManagers() {
        val gameDir = VldbFilesUtil.getDofusDirectory()
        val swfFile = File(gameDir, "DofusInvoker.swf")
        if (!swfFile.exists() || !swfFile.isFile) {
            throw RuntimeException("Unable to find DofusInvoker.swf in Dofus directory")
        }
        val builders = listOf(
            VldbIdByNameExportPackTaskBuilder("MessageReceiver", MessageIdByName, "_messagesTypes"),
            VldbIdByNameExportPackTaskBuilder("ProtocolTypeManager", TypeIdByName, "_typesTypes")
        )
        VldbProtocolUpdater.updateManagers(swfFile, builders)
        messagesById = Reflections(INetworkMessage::class.java.packageName)
            .getSubTypesOf(INetworkMessage::class.java)
            .map { (MessageIdByName.getId(it.simpleName) ?: error("Couldn't find id for [${it.simpleName}]")) to it }
            .toMap()
    }

    fun findServerIp(): String {
        val findServerIpProcessBuilder = ProcessBuilder(
            "cmd.exe", "/c", "netstat", "-a", "-p", "TCP", "-n",
            "|", "findstr", ":5555",
            "|", "findstr", "/V", ":5555[0-9]",
            "|", "findstr", "ESTABLISHED"
        )
        val process = findServerIpProcessBuilder.start()
        process.waitFor()
        val lines = BufferedReader(InputStreamReader(process.inputStream)).readLines()
        if (lines.isEmpty()) error("Couldn't find the server ip: The netstat command returned an empty result. Did you launch a Dofus session ?")
        lines.forEach { VldbLogger.debug("Netstat result : $it") }
        if (lines.size > 1) error("Couldn't find the server ip: The netstat command returned ${lines.size} results. Please only let one Dofus session opened ?")
        val words = lines[0].split(" ".toRegex()).toTypedArray()
        var ipAndPort: String? = null
        for (word in words) {
            if (word.isNotEmpty() && word.contains(":5555") && !word.contains("127.0.0.1")) {
                ipAndPort = word
            }
        }
        if (ipAndPort == null) error("Couldn't find the server ip: ip is null. Did you launch a Dofus session ?")
        val address = ipAndPort.split(":".toRegex()).toTypedArray()
        if (address.size != 2) error("Couldn't find the server ip: server address is " + address.size + " long. Did you launch a Dofus session ?")
        return address[0]
    }

}