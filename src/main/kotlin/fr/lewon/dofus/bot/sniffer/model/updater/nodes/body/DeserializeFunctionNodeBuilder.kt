package fr.lewon.dofus.bot.sniffer.model.updater.nodes.body

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.sniffer.model.updater.DESERIALIZE_FUNC_NAME
import fr.lewon.dofus.bot.sniffer.model.updater.FTKNodeBuilder
import fr.lewon.dofus.bot.sniffer.model.updater.FTKNodeDescription
import fr.lewon.dofus.bot.sniffer.model.updater.STREAM_NAME

class DeserializeFunctionNodeBuilder(nodeDescription: FTKNodeDescription) : FTKNodeBuilder(nodeDescription) {

    override fun getLines(): List<String> {
        return listOf("override fun $DESERIALIZE_FUNC_NAME($STREAM_NAME: ${ByteArrayReader::class.java.simpleName})")
    }

    override fun getSubNodeBuilders(): List<FTKNodeBuilder> {
        return listOf(
            CallSuperDeserializerNodeBuilder(nodeDescription),
            *FieldsSettersDeserializersNodeBuilder.fromFileDescription(nodeDescription).toTypedArray()
        )
    }
}