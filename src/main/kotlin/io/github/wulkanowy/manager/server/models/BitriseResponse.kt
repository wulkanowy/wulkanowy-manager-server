package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitriseResponse<T>(

    @SerialName("data")
    val data: T? = null,

    @SerialName("message")
    val message: String? = null,
)
