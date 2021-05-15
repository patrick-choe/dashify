package me.aroxu.dashify.server

import me.aroxu.dashify.DashifyPlugin.Companion.plugin
import me.aroxu.dashify.server.utils.Convertor
import me.aroxu.dashify.server.utils.SystemLoadChecker
import org.bukkit.GameRule

object InformationLoader {
    fun getWorldsInformation(): HashMap<String, Any> {
        val worldsMap = HashMap<String, Any>()
        plugin.server.worlds.forEach {
            val tempMap = HashMap<String, Any>()
            var entities = 0
            entities += it.entityCount
            tempMap["allowAnimals"] = it.allowAnimals
            tempMap["allowMonsters"] = it.allowMonsters
            tempMap["ambientSpawnLimit"] = it.ambientSpawnLimit
            tempMap["animalSpawnLimit"] = it.animalSpawnLimit
            tempMap["chunkCount"] = it.chunkCount
            tempMap["clearWeatherDuration"] = it.clearWeatherDuration
            tempMap["coordinateScale"] = it.coordinateScale
            tempMap["entitiesCount"] = entities
            tempMap["difficulty"] = it.difficulty
            tempMap["fullTime"] = it.fullTime
            tempMap["gameRules"] = it.gameRules.mapNotNull { ruleName ->
                GameRule.getByName(ruleName)?.let { rule ->
                    Pair(ruleName, it.getGameRuleValue(rule))
                }
            }.toMap()
            tempMap["isAutoSave"] = it.isAutoSave
            tempMap["seed"] = it.seed
            tempMap["isHardcore"] = it.isHardcore
            tempMap["pvp"] = it.pvp
            tempMap["players"] = it.players.map { player -> player.name }
            tempMap["keepSpawnInMemory"] = it.keepSpawnInMemory
            tempMap["viewDistance"] = it.viewDistance
            worldsMap[it.environment.toString()] = tempMap
        }
        return worldsMap
    }

    fun getTps(): DoubleArray {
        return plugin.server.tps
    }

    fun getCurrentPlayersSize(): Int {
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
            tempMap["affectsSpawning"] = it.affectsSpawning
            tempMap["clientViewDistance"] = it.clientViewDistance
            tempMap["cooldownPeriod"] = it.cooldownPeriod
            tempMap["exp"] = it.exp
            tempMap["healthScale"] = it.healthScale
            tempMap["health"] = it.health
            tempMap["level"] = it.level
            tempMap["scoreboard"] = it.scoreboard
            tempMap["scoreboardTag"] = it.scoreboardTags
            tempMap["totalExperience"] = it.totalExperience
            tempMap["walkSpeed"] = it.walkSpeed
            tempMap["raw"] = it.javaClass.declaredFields
                .map { mtd ->
                    mtd.isAccessible = true
                    return@map mtd.name to mtd.get(it)
                }
            playersMap[it.name] = tempMap
        }
        return playersMap
    }

    fun getMemoryStatus(): HashMap<String, Any> {
        val ramInformation = SystemLoadChecker.getRAMInformation()
        val memoryMap = HashMap<String, Any>()
        memoryMap["totalMemorySize"] = ramInformation[0]
        memoryMap["usedMemorySize"] = ramInformation[1]
        memoryMap["maxHeapSize"] = ramInformation[2]
        return memoryMap
    }
}
