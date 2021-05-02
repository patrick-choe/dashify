package me.aroxu.dashify.command

import com.github.monun.kommand.KommandBuilder
import com.github.monun.kommand.argument.string
import me.aroxu.dashify.config.DashifyConfigManager
import me.aroxu.dashify.version
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object Dashify {
    fun register(builder: KommandBuilder) {
        builder.apply {
            then("password") {
                require { isOp }
                then("password" to string()) {
                    executes {
                        it.sender.sendMessage("${ChatColor.GRAY}[INFO] Updating Password...")
                        it.sender.sendMessage(*ComponentBuilder("Click here").event(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, DashifyConfigManager.updatePassword(it.parseArgument("password")))).bold(true).underlined(true).append(" to copy authentication key.").bold(false).underlined(false).create())
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
