package io.github.wulkanowy.manager.server

import io.github.wulkanowy.manager.server.repositories.BitriseRepository
import io.github.wulkanowy.manager.server.repositories.GithubRepository
import io.github.wulkanowy.manager.server.services.BuildsService
import io.github.wulkanowy.manager.server.services.UnstableService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val mainModule = module {
    single { UnstableService(get()) }
    single { BuildsService(get()) }
    single { GithubRepository(get()) }
    single { BitriseRepository(get()) }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.INFO
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
            }
            defaultRequest {
                header("Authorization", "token " + System.getenv("API_KEY"))
            }
        }
    }
}
