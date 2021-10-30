package io.github.wulkanowy.manager.server

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.PullRequestBuild
import io.github.wulkanowy.manager.server.services.UnstableService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger
import org.slf4j.event.Level
import java.io.File

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        main()
    }.start(wait = true)
}

fun Application.main() {
    initializePlugins()
    initializeRouting()
}

fun Application.initializeRouting() {
    val unstableService: UnstableService by inject()

    routing {
        get("/") {
            call.respond(ApiResponse(success = true, data = "Welcome to Wulkanowy Manager Server!"))
        }
        get("/v1/stable") {
            call.respond(ApiResponse(success = true, data = listOf<PullRequestBuild>()))
        }
        get("/v1/develop") {
            call.respond(ApiResponse(success = true, data = listOf<PullRequestBuild>()))
        }
        get("/v1/unstable") {
            call.respond(unstableService.getLatestBuilds())
        }
        get("/v1/build/{branch}/artifacts/{index}/info") {
            call.respond("Branch: ${call.parameters["branch"]}")
        }
        static {
            staticRootFolder = File("src/main/resources/")
            file("favicon.ico")
        }
    }
}

fun Application.initializePlugins() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(HttpStatusCode.NotFound, ApiResponse(success = false, data = "${it.value} ${it.description}"))
        }
    }
    install(ContentNegotiation) {
        json()
    }
    install(Koin) {
        SLF4JLogger()
        modules(mainModule)
    }
}
