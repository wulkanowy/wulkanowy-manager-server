package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.BitriseBuildInfo
import io.github.wulkanowy.manager.server.models.PullRequestBuild
import io.github.wulkanowy.manager.server.models.PullRequestInfo
import io.github.wulkanowy.manager.server.repositories.BitriseRepository
import io.github.wulkanowy.manager.server.repositories.GithubRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class UnstableService(
    private val githubRepository: GithubRepository,
    private val bitriseRepository: BitriseRepository,
) {

    suspend fun getLatestBuilds(repoName: String, appId: String): ApiResponse<List<PullRequestBuild>> {
        val pullRequests = githubRepository.getPullRequests(repoName)

        return coroutineScope {
            val buildsWithPRs = pullRequests.map {
                async { it to bitriseRepository.getLastBuildsForBranch(appId, it.head.ref) }
            }.awaitAll()

            val builds = buildsWithPRs.map { (pr, builds) ->
                PullRequestBuild(
                    pullRequest = PullRequestInfo(
                        id = pr.id,
                        title = pr.title,
                        number = pr.number,
                        githubUrl = pr.htmlUrl,
                        userAvatarUrl = pr.user.avatarUrl,
                        userLogin = pr.user.login,
                        commitSha = pr.head.sha,
                    ),
                    build = builds.firstOrNull()?.let {
                        BitriseBuildInfo(
                            buildTimestamp = it.finishedAt,
                            buildSlug = it.buildSlug,
                            artifactSlug = it.artifactSlug,
                            buildNumber = it.buildNumber,
                        )
                    }
                )
            }

            ApiResponse(success = true, data = builds)
        }
    }
}
