package me.aroxu.dashify.config

import at.favre.lib.crypto.bcrypt.BCrypt
import me.aroxu.dashify.DashifyPlugin.Companion.plugin
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.aroxu.dashify.authKey
import me.aroxu.dashify.server.DashifyServer
import java.io.File

@Serializable
data class AuthenticationKey(val authKey: String)

@Serializable
data class ConfigDataStructure(
    val authKey: String = ""
)

object DashifyConfigurator {
    fun updateAuthKey(password: String): String {
        val json = Json { ignoreUnknownKeys = true }
        val hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val string = json.encodeToString(AuthenticationKey.serializer(), AuthenticationKey(hashed))
        saveToFile(string)
        authKey = hashed
        if (!DashifyServer.checkIsServerRunning()) {
            DashifyServer.restart()
        }
        return authKey
    }

    fun getAuthKey(): String {
        val jsonData = loadFromFile()
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString(AuthenticationKey.serializer(), jsonData).authKey
    }

    private fun saveToFile(jsonData: String) {
        val destinationFile = File(plugin.dataFolder, "config.json")
        destinationFile.absoluteFile.parentFile.mkdirs()
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
        }
        destinationFile.writeText(jsonData)
    }

    private fun loadFromFile(): String {
        val destinationFile = File(plugin.dataFolder, "config.json")
        if (!destinationFile.exists()) {
            plugin.logger.info("config.json not found! Generating file with default authkey...")
            saveToFile("{\"authKey\":\"\$2a\$12\$qg94dD9lyzgvhtXVyMkXIu8bX/GwU..3SlEvyzoAc2ZOrNbtPlWku\"}")
            plugin.logger.warning("For security, please change authkey using '/dashify key'")
        }
        val json = Json { ignoreUnknownKeys = true }
        return json.encodeToString(json.decodeFromString(ConfigDataStructure.serializer(), destinationFile.readText()))
    }
}
