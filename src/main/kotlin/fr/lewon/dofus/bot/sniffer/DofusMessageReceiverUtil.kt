package fr.lewon.dofus.bot.sniffer

import fr.lewon.dofus.VldbProtocolUpdater
import fr.lewon.dofus.bot.core.io.gamefiles.VldbFilesUtil
import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.managers.MessageIdByName
import fr.lewon.dofus.bot.sniffer.managers.TypeIdByName
import fr.lewon.dofus.bot.sniffer.model.messages.NetworkMessage
import fr.lewon.dofus.export.builder.VldbAbstractExportPackTaskBuilder
import fr.lewon.dofus.export.builder.VldbIdByNameExportPackTaskBuilder
import org.reflections.Reflections
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface

object DofusMessageReceiverUtil {

    private lateinit var messagesById: Map<Int, Class<out NetworkMessage>>

    fun parseMessagePremise(stream: ByteArrayReader, messageId: Int): DofusMessagePremise {
        val messageType = messagesById[messageId]
        val messageName = MessageIdByName.getName(messageId) ?: error("No message for id : $messageId")
        return DofusMessagePremise(messageName, messageId, messageType, stream)
    }

    fun prepareNetworkManagers(additionalBuilders: List<VldbAbstractExportPackTaskBuilder> = emptyList()) {
        processExport(getExportPackBuilders().union(additionalBuilders).toList())
        messagesById = Reflections(NetworkMessage::class.java.packageName)
            .getSubTypesOf(NetworkMessage::class.java)
            .map { (MessageIdByName.getId(it.simpleName) ?: null) to it }
            .filter { it.first != null }
            .associate { it.first!! to it.second }
    }

    private fun getExportPackBuilders(): List<VldbAbstractExportPackTaskBuilder> {
        return listOf(
            VldbIdByNameExportPackTaskBuilder("MessageReceiver", MessageIdByName, "_messagesTypes"),
            VldbIdByNameExportPackTaskBuilder("ProtocolTypeManager", TypeIdByName, "_typesTypes")
        )
    }

    private fun processExport(builders: List<VldbAbstractExportPackTaskBuilder>) {
        val gameDir = VldbFilesUtil.getDofusDirectory()
        val swfFile = File(gameDir, "DofusInvoker.swf")
        if (!swfFile.exists() || !swfFile.isFile) {
            throw RuntimeException("Unable to find DofusInvoker.swf in Dofus directory")
        }
        VldbProtocolUpdater.decompileSwf(swfFile, builders)
    }

    fun findInetAddress(networkInterfaceName: String): InetAddress? {
        return getNetworkInterfaces().firstOrNull { networkInterfaceName == it.displayName }
            ?.inetAddresses
            ?.asSequence()
            ?.firstOrNull(this::isAddressValid)
    }

    private fun isAddressValid(inetAddress: InetAddress): Boolean {
        return inetAddress.isSiteLocalAddress && !inetAddress.isLoopbackAddress
    }

    private fun getNetworkInterfaces(): List<NetworkInterface> {
        return NetworkInterface.getNetworkInterfaces().asSequence()
            .filter { it.isUp && !it.isLoopback && !it.displayName.contains("VMnet") }
            .toList()
    }

    fun getNetworkInterfaceNames(): List<String> {
        return getNetworkInterfaces().map { it.displayName }
    }
}