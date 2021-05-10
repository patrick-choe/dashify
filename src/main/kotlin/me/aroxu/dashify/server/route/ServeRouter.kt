package me.aroxu.dashify.server.route

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import me.aroxu.dashify.config.DashifyConfigurator
import me.aroxu.dashify.server.InformationLoader
import me.aroxu.dashify.version
import org.bukkit.World

data class AuthKey(val key: String)

fun Application.routeConfig() {
    suspend fun checkAuth(call:ApplicationCall): Map<String, Any>? {
        return if (call.request.headers.get("Dashify-Auth-Key") != DashifyConfigurator.getAuthKey()) {
            val authMap = HashMap<String, Any>()
            authMap["status"] = HttpStatusCode.Forbidden
            authMap["message"] = "Zurun! AuthKey mismatched with server!"
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
        get("/entities") {
            val authMap = checkAuth(call)
            if (authMap != null) {
                return@get
            } else {
                val entitiesMap = HashMap<String, Any>()
                entitiesMap["overworld"] = InformationLoader.getTotalEntitiesByWorldType(World.Environment.NORMAL)
                entitiesMap["nether"] = InformationLoader.getTotalEntitiesByWorldType(World.Environment.NETHER)
                entitiesMap["the_end"] = InformationLoader.getTotalEntitiesByWorldType(World.Environment.THE_END)
                call.respond(entitiesMap)
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
        get("/version") {
            val map = HashMap<String, Any>()
            map["version"] = version
            call.respond(map)
        }
    }
}