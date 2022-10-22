package fr.lewon.dofus.bot.sniffer.model.updater

import fr.lewon.dofus.bot.sniffer.DofusMessageReceiverUtil

fun main() = DofusMessageReceiverUtil.prepareNetworkManagers(listOf(MessageExportBuilder))