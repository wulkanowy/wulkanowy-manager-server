package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitriseBuildItem(

    @SerialName("slug")
    val slug: String,
)
