package me.aroxu.dashify

import com.github.monun.kommand.kommand
import me.aroxu.dashify.command.Dashify
import me.aroxu.dashify.config.DashifyConfigurator
import me.aroxu.dashify.server.DashifyServer
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author aroxu
 */
var authKey: String = ""

class DashifyPlugin : JavaPlugin() {
    private val defaultAuthKey: String = "\$2a\$12\$qg94dD9lyzgvhtXVyMkXIu8bX/GwU..3SlEvyzoAc2ZOrNbtPlWku"

    companion object {
        lateinit var plugin: DashifyPlugin
            private set
        lateinit var version: String
            private set
    }

    override fun onEnable() {
        plugin = this
        version = description.version
        logger.info("NOTE: IF YOU HAVE ANY ISSUE WHILE USING THIS PLUGIN, REPORT THE ISSUE AT GITHUB: \"https://github.com/aroxu/dashify/issues\"")
        logger.info("Dashify v$version has loaded.")
        kommand {
            register("dashify") {
                Dashify.register(this)
            }
        }
        authKey = DashifyConfigurator.getAuthKey()
        if (authKey == defaultAuthKey) {
            logger.warning("Auth Key is the same as the default key!")
            logger.warning("Dashify server won't start until the key is changed.")
            return
        }
        DashifyServer.start()
        logger.info("Started Dashify Server.")
    }

    override fun onDisable() {
        logger.info("Disabled Dashify v$version.")
        DashifyServer.stop()
        logger.info("Stopped Dashify Server.")
    }
}
