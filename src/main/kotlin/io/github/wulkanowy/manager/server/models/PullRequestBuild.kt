package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.Serializable

@Serializable
data class PullRequestBuild(
    val id: Long,
    val title: String,
    val number: Int,
    val buildTimestamp: String,
    val githubUrl: String,
    val buildSlug: String,
    val artifactSlug: String,
    val buildNumber: Int,
    val userAvatarUrl: String,
    val userLogin: String,
    val commitSha: String,
)
