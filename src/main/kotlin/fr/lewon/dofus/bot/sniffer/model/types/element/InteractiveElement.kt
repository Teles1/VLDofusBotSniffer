package fr.lewon.dofus.bot.sniffer.model.types.element

import fr.lewon.dofus.bot.core.io.stream.ByteArrayReader
import fr.lewon.dofus.bot.core.manager.d2o.D2OUtil
import fr.lewon.dofus.bot.core.manager.i18n.I18NUtil
import fr.lewon.dofus.bot.sniffer.model.INetworkType
import fr.lewon.dofus.bot.sniffer.model.TypeManager

open class InteractiveElement : INetworkType {

    var elementId = -1
    var elementTypeId = -1
    var enabledSkills = ArrayList<InteractiveElementSkill>()
    var disabledSkills = ArrayList<InteractiveElementSkill>()
    var onCurrentMap = false

    override fun deserialize(stream: ByteArrayReader) {
        elementId = stream.readInt()
        elementTypeId = stream.readInt()
        println("---")
        for (i in 0 until stream.readUnsignedShort()) {
            val elementSkill = TypeManager.getInstance<InteractiveElementSkill>(stream.readUnsignedShort())
            elementSkill.deserialize(stream)
            enabledSkills.add(elementSkill)
            println("SKILL : ${elementSkill.skillId}")
            printSkill(elementSkill.skillId)
        }
        for (i in 0 until stream.readUnsignedShort()) {
            val elementSkill = TypeManager.getInstance<InteractiveElementSkill>(stream.readUnsignedShort())
            elementSkill.deserialize(stream)
            disabledSkills.add(elementSkill)
        }
        onCurrentMap = stream.readBoolean()
        println("ELEMENT : $elementId / $elementTypeId")
        printInteractive(elementTypeId)
        println(onCurrentMap)
    }

    private fun printInteractive(elementTypeId: Int) {
        D2OUtil.getObjects("Interactives").firstOrNull { it["id"]?.equals(elementTypeId) ?: false }
            ?.let {
                println(it)
                println(I18NUtil.getLabel(it["nameId"].toString().toInt()))
            } ?: println("NOT FOUND")
    }

    private fun printSkill(skillId: Int) {
        D2OUtil.getObjects("Skills").firstOrNull { it["id"]?.equals(skillId) ?: false }
            ?.let {
                println(it)
                println(I18NUtil.getLabel(it["nameId"].toString().toInt()))
                printInteractive(it["interactiveId"].toString().toInt())
            } ?: println("NOT FOUND")
    }

}