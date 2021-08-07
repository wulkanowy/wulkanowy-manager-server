package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubPullRequestsItem(
    @SerialName("title")
    val title: String,

    @SerialName("number")
    val number: Int,

    @SerialName("html_url")
    val htmlUrl: String,

    @SerialName("user")
    val user: GithubUser,

    @SerialName("head")
    val head: Head,

    @SerialName("id")
    val id: Long,
)

@Serializable
data class GithubUser(

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("login")
    val login: String,
)

@Serializable
data class Head(

    @SerialName("ref")
    val ref: String,

    @SerialName("sha")
    val sha: String,
)
