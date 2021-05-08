package me.aroxu.dashify.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.aroxu.dashify.server.route.routeConfig

object DashifyServer {
    private val server = embeddedServer(Netty, port = 9080, host = "0.0.0.0") {
        routeConfig()
    }
    fun start() {
        server.start(wait = true)
    }
    fun stop() {
        server.stop(20, 20)
    }
}