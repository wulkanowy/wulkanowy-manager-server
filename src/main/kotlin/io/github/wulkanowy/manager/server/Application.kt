package io.github.wulkanowy.manager.server

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.PullRequestBuild
import io.github.wulkanowy.manager.server.services.BuildsService
import io.github.wulkanowy.manager.server.services.UnstableService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
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
    initializeCaching()
    initializeRouting()
}

fun Application.initializeRouting() {
    val unstableService: UnstableService by inject()
    val buildsService: BuildsService by inject()

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
        get("/v1/pr/app/{appId}/repo/{repo}") {
            call.respond(
                unstableService.getLatestBuilds(
                    appId = call.parameters["appId"]!!,
                    repoName = call.parameters["repo"]!!,
                )
            )
        }
        get("/v1/build/app/{appId}/branch/{branch...}") {
            call.respond(
                buildsService.getLastBuildFromBranch(
                    appId = call.parameters["appId"]!!,
                    branchName = call.parameters.getAll("branch")!!.joinToString("/")
                )
            )
        }
        get("/v1/download/app/{appId}/branch/{branch...}") {
            val redirectUrl = buildsService.getDownloadPageRedirect(
                appId = call.parameters["appId"]!!,
                branchName = call.parameters.getAll("branch")!!.joinToString("/")
            )
            call.respondRedirect(redirectUrl, permanent = false)
        }
        get("/v1/download/app/{appId}/build/{buildId}/artifact/{artifactId}") {
            val redirectUrl = buildsService.getDownloadPageRedirect(
                appId = call.parameters["appId"]!!,
                buildId = call.parameters["buildId"]!!,
                artifactId = call.parameters["artifactId"]!!,
            )
            call.respondRedirect(redirectUrl, permanent = false)
        }
        static {
            staticRootFolder = File("src/main/resources/")
            file("favicon.ico")
        }
    }
}

fun Application.initializeCaching() {
    install(DefaultHeaders) {
        header("Cloudflare-CDN-Cache-Control", "max-age=3600")
        header("CDN-Cache-Control", "3600")
    }
}

fun Application.initializePlugins() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = ApiResponse<String>(success = false, error = "${status.value} ${status.description}")
            )
        }
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ApiResponse<String>(success = false, error = cause.message)
            )
            throw cause
        }
    }
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        anyHost()
    }
    install(Koin) {
        SLF4JLogger()
        modules(mainModule)
    }
}
