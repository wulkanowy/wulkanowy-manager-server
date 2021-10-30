package io.github.wulkanowy.manager.server

import io.github.wulkanowy.manager.server.repositories.BitriseRepository
import io.github.wulkanowy.manager.server.repositories.GithubRepository
import io.github.wulkanowy.manager.server.services.BuildsService
import io.github.wulkanowy.manager.server.services.UnstableService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.cache.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
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
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
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
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
            }
            install(HttpCache)
            defaultRequest {
//                header("Authorization", "token " + System.getenv("GITHUB_API_KEY"))
            }
        }
    }
}
