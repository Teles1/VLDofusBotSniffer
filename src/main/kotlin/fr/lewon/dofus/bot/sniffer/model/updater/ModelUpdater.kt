package fr.lewon.dofus.bot.sniffer.model.updater

import fr.lewon.dofus.bot.sniffer.DofusMessageReceiverUtil

class ModelUpdater {
}

fun main() {
    DofusMessageReceiverUtil.prepareNetworkManagers(
        listOf(
            MessageExportBuilder
        )
    )
}