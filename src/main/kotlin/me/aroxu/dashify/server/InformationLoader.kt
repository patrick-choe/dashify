package me.aroxu.dashify.server

import me.aroxu.dashify.DashifyPlugin.Companion.plugin
import me.aroxu.dashify.server.utils.Convertor
import org.bukkit.World

object InformationLoader {
    fun getTotalEntitiesByWorldType(environmentType: World.Environment): Int {
        var entities = 0

        plugin.server.worlds.forEach {
            if (it.environment == environmentType) {
                entities += it.entityCount
            }
        }
        return entities
    }

    fun getTps(): DoubleArray {
        return plugin.server.tps
    }

    fun getPlayerCount(): Int {
        return plugin.server.onlinePlayers.size
    }

    fun getPlayersStatus(): HashMap<String, Any> {
        val playersMap = HashMap<String, Any>()
        plugin.server.onlinePlayers.forEach {
            val tempMap = HashMap<String, Any>()
            val clientName: String = if (it.clientBrandName == null) {
                "Null"
            } else {
                it.clientBrandName!!
            }
            tempMap["address"] = it.address
            tempMap["receivedClientName"] = clientName
            tempMap["latency"] = it.spigot().ping
            tempMap["uuid"] = it.uniqueId
            tempMap["isFlying"] = it.isFlying
            tempMap["allowFlight"] = it.allowFlight
            tempMap["flySpeed"] = it.flySpeed
            tempMap["hasResourcePack"] = it.hasResourcePack()
            tempMap["detailedProfile"] = it.playerProfile
            tempMap["playTime"] = Convertor.convertLongToDuration(it.playerTime)
            tempMap["firstPlayTime"] = Convertor.convertLongToTime(it.firstPlayed)
            tempMap["lastJoinTime"] = Convertor.convertLongToTime(it.lastSeen)
            playersMap[it.name] = tempMap
        }
        return playersMap
    }
}
