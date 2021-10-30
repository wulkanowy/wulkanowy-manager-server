package io.github.wulkanowy.manager.server

import io.github.wulkanowy.manager.server.repositories.GithubRepository
import io.github.wulkanowy.manager.server.services.UnstableService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.koin.dsl.module

val mainModule = module {
    single { UnstableService(get()) }
    single { GithubRepository(get()) }
    single {
        HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true

                })
            }
        }
    }
}
