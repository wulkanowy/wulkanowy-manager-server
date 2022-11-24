package io.github.wulkanowy.manager.server

import io.github.wulkanowy.manager.server.repositories.BitriseRepository
import io.github.wulkanowy.manager.server.repositories.GithubRepository
import io.github.wulkanowy.manager.server.services.BuildsService
import io.github.wulkanowy.manager.server.services.UnstableService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single { UnstableService(get(), get()) }
    single { BuildsService(get()) }
    single { GithubRepository(get(named("github"))) }
    single { BitriseRepository(get(named("bitrise"))) }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single(named("bitrise")) {
        HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(HttpCache)
            defaultRequest {
                header("Authorization", "token " + System.getenv("BITRISE_API_KEY"))
            }
        }
    }
    single(named("github")) {
        HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json()
            }
            install(HttpCache)
            defaultRequest {
                System.getenv("API_GITHUB_TOKEN").takeIf { !it.isNullOrBlank() }?.let {
                    header("Authorization", "token $it")
                }
            }
        }
    }
}
