package me.aroxu.dashify.command

import me.aroxu.dashify.config.DashifyConfigManager
import me.aroxu.dashify.version
import com.github.monun.kommand.KommandBuilder
import com.github.monun.kommand.argument.string
import org.bukkit.ChatColor

object Dashify {
    fun register(builder: KommandBuilder) {
        builder.apply {
            then("password") {
                require { isOp }
                then("password" to string()) {
                    executes {
                        it.sender.sendMessage("${ChatColor.GRAY}[INFO] Updating Password...")
                        DashifyConfigManager.updatePassword(it.parseArgument("password"))
                        it.sender.sendMessage("${ChatColor.AQUA}[SUCCESS] Password Updated!")
                    }
                }
            }
            then("version") {
                executes {
                    it.sender.sendMessage("Dashify v.${version} by aroxu")
                }
            }
        }
    }
}
