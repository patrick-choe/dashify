package me.aroxu.dashify.server.route

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.response.*
import me.aroxu.dashify.server.InformationLoader
import me.aroxu.dashify.version
import org.bukkit.World
import java.lang.Compiler.enable

fun Application.routeConfig() {
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
            val map = HashMap<String, Any>()
            map["overworld"] = InformationLoader.getTotalEntitiesByWorldType(World.Environment.NORMAL)
            map["nether"] = InformationLoader.getTotalEntitiesByWorldType(World.Environment.NETHER)
            map["the_end"] = InformationLoader.getTotalEntitiesByWorldType(World.Environment.THE_END)
            call.respond(map)
        }
        get("/tps") {
            call.respond(InformationLoader.getTps())
        }
        get("/version") {
            val map = HashMap<String, Any>()
            map["version"] = version
            call.respond(map)
        }
    }
}