package fr.lewon.dofus.bot.sniffer.model

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader

interface INetworkType {

    fun deserialize(stream: ByteArrayReader)

}