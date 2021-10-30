package io.github.wulkanowy.manager.server.repositories

import io.github.wulkanowy.manager.server.models.GithubPullRequestsItem
import io.ktor.client.*
import io.ktor.client.request.*

class GithubRepository constructor(private val client: HttpClient) {

    companion object {
        private const val GITHUB_BASE_URL = "https://api.github.com/repos/wulkanowy"
        private const val PULL_REQUESTS = "pulls?state=open"
    }

    suspend fun getPullRequests(repoName: String): List<GithubPullRequestsItem> =
        client.get("$GITHUB_BASE_URL/$repoName/$PULL_REQUESTS")
}
