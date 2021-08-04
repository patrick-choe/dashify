package me.aroxu.dashify.server.route

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json
import kotlinx.serialization.json.Json
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
        json(Json {
            prettyPrint = true
        })
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