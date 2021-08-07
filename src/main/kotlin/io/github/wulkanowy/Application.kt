package io.github.wulkanowy

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.github.wulkanowy.plugins.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        configureMonitoring()
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
