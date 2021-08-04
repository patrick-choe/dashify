package me.aroxu.dashify.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import me.aroxu.dashify.DashifyPlugin.Companion.version
import me.aroxu.dashify.config.DashifyConfigurator
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object Dashify {
    fun register(builder: LiteralNode) {
        builder.apply {
            then("key") {
                then("key" to string()) {
                    requires { sender.isOp }
                    executes { context ->
                        if (sender !is Player) {
                            val key: String by context
                            if (key.length < 8) {
                                sender.sendMessage("${ChatColor.RED}[ERR] Please enter a password of 8 digits or longer!")
                                return@executes
                            }
                            val numberIncluded = key.filter { it.isDigit() }
                            val stringIncluded = key.filter { it.isLetter() }
                            if (numberIncluded.isEmpty()) {
                                sender.sendMessage("${ChatColor.RED}[ERR] Passwords must contain at least one number!")
                                return@executes
                            } else if (stringIncluded.isEmpty()) {
                                sender.sendMessage("${ChatColor.RED}[ERR] Passwords must contain at least one string!")
                                return@executes
                            }
                            sender.sendMessage("${ChatColor.GRAY}[INFO] Updating Access Key...")
                            sender.sendMessage(
                                *ComponentBuilder("Your Access key is: ").append(
                                    "\"${
                                        DashifyConfigurator.updateAuthKey(key)
                                    }\". "
                                ).bold(true).underlined(false).append("DO NOT SHARE THIS KEY.").bold(true)
                                    .underlined(true).color(
                                        net.md_5.bungee.api.ChatColor.DARK_RED
                                    ).create()
                            )
                            sender.sendMessage("${ChatColor.AQUA}[SUCCESS] Access Key Updated!")
                        } else {
                            sender.sendMessage("${ChatColor.RED}[ERR] Please run this command on console!")
                            return@executes
                        }
                    }
                }
            }
            then("version") {
                executes {
                    sender.sendMessage("Dashify v${version} by aroxu")
                }
            }
        }
    }
}
