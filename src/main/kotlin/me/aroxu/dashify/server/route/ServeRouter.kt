package me.aroxu.dashify.server.route

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import me.aroxu.dashify.DashifyPlugin.Companion.version
import me.aroxu.dashify.authKey
import me.aroxu.dashify.server.InformationLoader

fun Application.routeConfig() {
    suspend fun checkAuth(call: ApplicationCall): Map<String, Any>? {
        return if (call.request.headers["Dashify-Auth-Key"] != authKey) {
            val authMap = HashMap<String, Any>()
            authMap["status"] = HttpStatusCode.Forbidden
            authMap["message"] = "Oops! AuthKey mismatched with server!"
            call.respond(HttpStatusCode.Forbidden, authMap)
            authMap
        } else null
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondRedirect("/version")
        }
        get("/worlds") {
            val authMap = checkAuth(call)
            if (authMap != null) {
                return@get
            } else {
                call.respond(InformationLoader.getWorldsInformation())
            }
        }
        get("/tps") {
            val authMap = checkAuth(call)
            if (authMap != null) {
                return@get
            } else {
                call.respond(InformationLoader.getTps())
            }
        }
        get("/players") {
            val authMap = checkAuth(call)
            if (authMap != null) {
                return@get
            } else {
                val playersMap = HashMap<String, Any>()
                playersMap["currentPlayerSize"] = InformationLoader.getCurrentPlayersSize()
                playersMap["maxPlayersSize"] = InformationLoader.getMaxPlayersSize()
                if (InformationLoader.getCurrentPlayersSize() >= 1) {
                    playersMap["players"] = InformationLoader.getPlayersStatus()
                }
                call.respond(playersMap)
            }
        }
        get("/memory") {
            val authMap = checkAuth(call)
            if (authMap != null) {
                return@get
            } else {
                call.respond(InformationLoader.getMemoryStatus())
            }
        }
        get("/processor") {
            val authMap = checkAuth(call)
            if (authMap != null) {
                return@get
            } else {
                call.respond(InformationLoader.getProcessorStatus())
            }
        }
        get("/os") {
            val authMap = checkAuth(call)
            if (authMap != null) {
                return@get
            } else {
                call.respond(InformationLoader.getOsInformation())
            }
        }
        get("/version") {
            call.respond(mapOf("version" to "v$version"))
        }
    }
}