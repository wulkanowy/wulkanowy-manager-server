package io.github.wulkanowy.manager.server.repositories

import io.github.wulkanowy.manager.server.models.BitriseBuild
import io.github.wulkanowy.manager.server.models.GithubPullRequestsItem
import io.github.wulkanowy.manager.server.models.PullRequestBuild
import io.ktor.client.*
import io.ktor.client.request.*

class GithubRepository constructor(private val client: HttpClient) {

    companion object {
        private const val GITHUB_BASE_URL = "https://api.github.com/repos/wulkanowy/wulkanowy"
        private const val PULL_REQUESTS = "$GITHUB_BASE_URL/pulls?state=open"

        private const val BITRISE_BASE = "https://bitrise-redirector.herokuapp.com/v0.1/apps/daeff1893f3c8128/builds"
        private const val BITRISE_ARTIFACT = "artifacts/0/info"
    }

    suspend fun getPullRequests() = client.get<List<GithubPullRequestsItem>>(PULL_REQUESTS).map {
        val build = client.get<BitriseBuild>("$BITRISE_BASE/${it.head.ref}/$BITRISE_ARTIFACT")
        PullRequestBuild(
            title = it.title,
            number = it.number,
            buildTimestamp = build.finishedAt,
            githubUrl = it.htmlUrl,
            downloadUrl = build.publicInstallPageUrl,
            buildUrl = build.buildUrl,
            buildNumber = build.buildNumber,
            userAvatarUrl = it.user.avatarUrl,
            userLogin = it.user.login,
            commitSha = it.head.sha,
            id = it.id,
        )
    }
}
