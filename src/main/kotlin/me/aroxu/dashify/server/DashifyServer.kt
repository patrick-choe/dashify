package me.aroxu.dashify.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.aroxu.dashify.server.route.routeConfig

object DashifyServer {
    private var isServerRunning: Boolean = false
    private val server = embeddedServer(Netty, port = 1972, host = "0.0.0.0") {
        routeConfig()
    }

    fun start() {
        if (isServerRunning) return
        Thread {
            server.start(wait = true)
        }.start()
        isServerRunning = true
    }

    fun stop() {
        if (!isServerRunning) return
        server.stop(20, 20)
        isServerRunning = false
    }

    fun restart() {
        stop()
        start()
    }

    fun checkIsServerRunning(): Boolean {
        return isServerRunning
    }
}