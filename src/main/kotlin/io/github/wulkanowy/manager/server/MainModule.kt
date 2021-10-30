package io.github.wulkanowy.manager.server

import io.github.wulkanowy.manager.server.repositories.GithubRepository
import io.github.wulkanowy.manager.server.services.UnstableService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val mainModule = module {
    single { UnstableService(get()) }
    single { GithubRepository(get()) }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
            }
        }
    }
}
