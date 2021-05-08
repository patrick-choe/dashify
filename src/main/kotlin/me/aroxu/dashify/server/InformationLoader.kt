package me.aroxu.dashify.server

import me.aroxu.dashify.DashifyPlugin.Companion.plugin
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
}
