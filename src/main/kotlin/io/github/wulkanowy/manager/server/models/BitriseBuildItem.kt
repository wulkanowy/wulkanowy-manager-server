package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitriseBuildItem(

    @SerialName("slug")
    val slug: String,

    @SerialName("build_number")
    val buildNumber: Int,

    @SerialName("commit_view_url")
    val commitViewUrl: String,

    @SerialName("finished_at")
    val finishedAt: String,
)
