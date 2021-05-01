package me.aroxu.dashify.command

import me.aroxu.dashify.config.MireWallDataManager
import me.aroxu.dashify.version
import com.github.monun.kommand.KommandBuilder
import com.github.monun.kommand.argument.player
import com.github.monun.kommand.argument.string

object Dashify {
    fun register(builder: KommandBuilder) {
        builder.apply {
            then("password") {
                    then("password" to string()) {
                        executes {
                            MireWallDataManager.setPassword(it.parseArgument("password"))
                        }
                    }
            }
            then("version") {
                executes {
                    it.sender.sendMessage("MireWall v.${version} by aroxu")
                }
            }
        }
    }
}
