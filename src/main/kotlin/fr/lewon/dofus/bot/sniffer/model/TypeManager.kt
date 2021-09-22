package fr.lewon.dofus.bot.sniffer.model

import fr.lewon.dofus.bot.sniffer.managers.TypeIdByName
import org.reflections.Reflections


object TypeManager {

    private val typeByName = HashMap<String, Class<out INetworkType>>()

    init {
        Reflections(javaClass.packageName).getSubTypesOf(INetworkType::class.java).forEach {
            typeByName[it.simpleName] = it
        }
    }

    fun <T : INetworkType> getInstance(id: Int): T {
        val scriptName = TypeIdByName.getName(id) ?: error("Couldn't find name for id [$id]")
        val refClass = typeByName[scriptName] ?: error("No script found for name [$scriptName]")
        return refClass.getConstructor().newInstance() as T
    }

}