package me.aroxu.dashify.config

import at.favre.lib.crypto.bcrypt.BCrypt
import me.aroxu.dashify.plugin
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AuthenticationPassword(val password: String)

@Serializable
data class ConfigDataStructure(
    val password: String = ""
)

object DashifyConfigManager {
    fun updatePassword(password: String): String {
        val json = Json { ignoreUnknownKeys = true }
        val hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val string = json.encodeToString(AuthenticationPassword.serializer(), AuthenticationPassword(hashed))
        saveToFile(string)
        return hashed
    }

    private fun saveToFile(jsonData: String) {
        val destinationFile = File(plugin.dataFolder, "config.json")
        destinationFile.absoluteFile.parentFile.mkdirs()
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
        }
        destinationFile.writeText(jsonData)
    }

    fun loadFromFile(): String {
        val destinationFile = File(plugin.dataFolder, "config.json")
        if (!destinationFile.exists()) {
            return "Cannot find that file"
        }
        val json = Json { ignoreUnknownKeys = true }
        return json.encodeToString(json.decodeFromString(ConfigDataStructure.serializer(), destinationFile.readText()))
    }
}
