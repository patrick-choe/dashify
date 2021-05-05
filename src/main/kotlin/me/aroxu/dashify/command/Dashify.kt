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
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object Dashify {
    fun register(builder: KommandBuilder) {
        builder.apply {
            then("key") {
                then("key" to string()) {
                    require { isOp }
                    executes {
                        if (it.sender !is Player) {
                            if (it.parseArgument<String>("key").length < 8) {
                                it.sender.sendMessage("${ChatColor.RED}[ERR] Please enter a password of 8 digits or longer!")
                                return@executes
                            }
                            val numberIncluded = it.parseArgument<String>("key").filter { it.isDigit() }
                            if (numberIncluded.isEmpty()) {
                                it.sender.sendMessage("${ChatColor.RED}[ERR] Passwords must contain at least one number!")
                                return@executes
                            }
                            it.sender.sendMessage("${ChatColor.GRAY}[INFO] Updating Access Key...")
//                        it.sender.sendMessage(*ComponentBuilder("Click here").event(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, DashifyConfigManager.updatePassword(it.parseArgument("password")))).bold(true).underlined(true).append(" to copy authentication key.").bold(false).underlined(false).create())
                            it.sender.sendMessage(*ComponentBuilder("Your Access key is: ").append("${DashifyConfigManager.updatePassword(it.parseArgument("key"))}. ").bold(true).underlined(false).append("DO NOT SHARE THIS KEY.").bold(true).underlined(true).color(
                                net.md_5.bungee.api.ChatColor.DARK_RED).create())
                            it.sender.sendMessage("${ChatColor.AQUA}[SUCCESS] Access Key Updated!")
                        }
                        else {
                            it.sender.sendMessage("${ChatColor.RED}[ERR] Please run this command on console!")
                            return@executes
                        }
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
