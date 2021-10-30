package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitriseBuild(

    @SerialName("build_number")
    val buildNumber: Int,

    @SerialName("build_slug")
    val buildSlug: String,

    @SerialName("artifact_slug")
    val artifactSlug: String,

    @SerialName("commit_view_url")
    val commitViewUrl: String,

    @SerialName("finished_at")
    val finishedAt: String,
)
