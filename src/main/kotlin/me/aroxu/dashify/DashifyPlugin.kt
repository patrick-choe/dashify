package me.aroxu.dashify

import com.github.monun.kommand.kommand
import me.aroxu.dashify.command.Dashify
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author aroxu
 */
val version: String = "0.0.1"
lateinit var plugin: DashifyPlugin
    private set
class DashifyPlugin : JavaPlugin() {
    override fun onEnable(){
        plugin = this
        logger.info("Dashify v.$version has loaded.")
        kommand {
            register("dashify") {
                Dashify.register(this)
            }
        }
    }
}
