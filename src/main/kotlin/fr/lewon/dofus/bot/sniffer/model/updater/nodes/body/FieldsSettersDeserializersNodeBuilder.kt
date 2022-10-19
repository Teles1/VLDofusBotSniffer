package fr.lewon.dofus.bot.sniffer.model.updater.nodes.body

import fr.lewon.dofus.bot.sniffer.model.ProtocolTypeManager
import fr.lewon.dofus.bot.sniffer.model.updater.DESERIALIZE_FUNC_NAME
import fr.lewon.dofus.bot.sniffer.model.updater.FTKNodeBuilder
import fr.lewon.dofus.bot.sniffer.model.updater.FTKNodeDescription
import fr.lewon.dofus.bot.sniffer.model.updater.STREAM_NAME
import fr.lewon.dofus.bot.sniffer.model.updater.nodes.body.fields.VariableDeclaration
import fr.lewon.dofus.bot.sniffer.model.updater.nodes.body.fields.VariableType
import fr.lewon.dofus.bot.sniffer.model.updater.nodes.body.fields.VariableUtils

class FieldsSettersDeserializersNodeBuilder(
    nodeDescription: FTKNodeDescription,
    private val variable: VariableDeclaration
) : FTKNodeBuilder(nodeDescription) {

    companion object {
        fun fromFileDescription(nodeDescription: FTKNodeDescription): List<FieldsSettersDeserializersNodeBuilder> {
            return nodeDescription.variables.map { FieldsSettersDeserializersNodeBuilder(nodeDescription, it) }
        }
    }

    override fun getLines(): List<String> = doGetLines(variable, nodeDescription.fileContent)

    private fun doGetLines(variableDeclaration: VariableDeclaration, fileContent: String): List<String> {
        return when (variableDeclaration.variableType) {
            VariableType.INT, VariableType.INT2, VariableType.DOUBLE ->
                listOf(getNumberSetter(variableDeclaration, fileContent))
            VariableType.BOOLEAN -> getBooleanSetterLines(variableDeclaration, fileContent)
            VariableType.STRING -> listOf(getAssignation(variableDeclaration, "$STREAM_NAME.readUTF()"))
            VariableType.BYTE_ARRAY -> getByteArraySetterLines()
            VariableType.LIST -> getListSetterLines(variableDeclaration, fileContent)
            null -> getObjectSetter(variableDeclaration, fileContent)
        }
    }

    private fun getNumberSetter(variableDeclaration: VariableDeclaration, fileContent: String): String {
        val methodCall = Regex("${variableDeclaration.name} = input\\.(.*?);")
            .find(fileContent)
            ?.destructured?.component1()
            ?.replace("readVarUh", "readVar")
            ?.replace("readShort", "readUnsignedShort")
            ?.replace("readByte", "readUnsignedByte")
            ?.replace("readUnsignedInt", "readInt")
            ?: error("Couldn't find setter for field ${variableDeclaration.name} (${nodeDescription.name})")
        val convertCall = variableDeclaration.variableType?.kotlinType
            ?.let { ".to$it()" }
            ?: ""
        return getAssignation(variableDeclaration, "$STREAM_NAME.$methodCall$convertCall")
    }

    private fun getBooleanSetterLines(variableDeclaration: VariableDeclaration, fileContent: String): List<String> {
        val lines = ArrayList<String>()
        if (fileContent.contains("${variableDeclaration.name} = BooleanByteWrapper")) {
            val matchResult = Regex("${variableDeclaration.name} = BooleanByteWrapper\\.getFlag\\((.*?),(.*?)\\);")
                .find(fileContent)
                ?: error("Couldn't find boolean assignation : ${variableDeclaration.name} (${nodeDescription.name})")
            val boxName = matchResult.destructured.component1()
            val boxIndex = matchResult.destructured.component2().toInt()
            if (boxIndex == 0) {
                lines.add("val $boxName = $STREAM_NAME.readByte()")
            }
            lines.add(getAssignation(variableDeclaration, "BooleanByteWrapper.getFlag($boxName, $boxIndex)"))
        } else {
            lines.add(getAssignation(variableDeclaration, "$STREAM_NAME.readBoolean()"))
        }
        return lines
    }

    private fun getObjectSetter(variableDeclaration: VariableDeclaration, fileContent: String): List<String> {
        val objValue = if (fileContent.contains("${variableDeclaration.name} = ProtocolTypeManager")) {
            "${ProtocolTypeManager::class.java.simpleName}.getInstance<${variableDeclaration.kotlinType}>($STREAM_NAME.readUnsignedShort())"
        } else {
            "${variableDeclaration.kotlinType}()"
        }
        return listOf(
            getAssignation(variableDeclaration, objValue),
            "${variableDeclaration.name}.$DESERIALIZE_FUNC_NAME($STREAM_NAME)"
        )
    }

    private fun getByteArraySetterLines(): List<String> {
        val lengthVarName = "${variable.name}Length"
        return listOf(
            "val $lengthVarName = $STREAM_NAME.readVarInt()",
            "${variable.name} += $STREAM_NAME.readNBytes($lengthVarName)"
        )
    }

    private fun getAssignation(variableDeclaration: VariableDeclaration, value: String): String =
        "${variableDeclaration.name} = $value"

    private fun getListSetterLines(variableDeclaration: VariableDeclaration, fileContent: String): List<String> {
        val variableName = variableDeclaration.name
        val setMethodPattern = "function _${variableName}Func\\(.*?\\).*?\\{.*?(push.*?}|\\+\\+.*?})"
        val setMethodMatchResult = Regex(setMethodPattern, RegexOption.DOT_MATCHES_ALL).find(fileContent)
            ?: error("Couldn't find set method for : $variableName (${nodeDescription.name})")
        val setMethodContent = setMethodMatchResult.value
        val assignedItemName = Regex("$variableName\\.push\\((.*?)\\)").find(setMethodContent)
            ?: Regex("($variableName\\[.*?])").find(setMethodContent)
            ?: error("Couldn't find assigned item name : $variableName (${nodeDescription.name})")
        var itemName = assignedItemName.destructured.component1()
        val listTypeStr = VariableUtils.getListTypeStr(nodeDescription, variableDeclaration.name)
        if (setMethodContent.contains("$variableName.push(")) {
            itemName += ":$listTypeStr"
        } else {
            itemName = itemName.replace("[", "\\[")
        }
        val listType = VariableType.fromFlashType(listTypeStr)
        val kotlinType = listType?.kotlinType ?: listTypeStr
        val subVariableDeclaration = VariableDeclaration(itemName, kotlinType, "", listType)
        val itemAssociationLines = doGetLines(subVariableDeclaration, setMethodContent).map {
            it.replace(itemName, "item")
        }
        val itemInitLine = itemAssociationLines.firstOrNull()?.let { "\tval $it" }
            ?: error("No init line for variable $variableName (${nodeDescription.name})")
        val additionalItemInitLines = if (itemAssociationLines.size > 1) {
            itemAssociationLines.subList(1, itemAssociationLines.size).map { "\t$it" }
        } else emptyList()
        return listOf(
            getAssignation(variableDeclaration, "ArrayList()"),
            "for (i in 0 until $STREAM_NAME.readUnsignedShort()) {",
            itemInitLine,
            *additionalItemInitLines.toTypedArray(),
            "\t${variableDeclaration.name}.add(item)",
            "}"
        )
    }

    override fun getSubNodeBuilders(): List<FTKNodeBuilder> {
        return emptyList()
    }

}
