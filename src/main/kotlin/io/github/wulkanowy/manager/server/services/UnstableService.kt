package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.PullRequestBuild
import io.github.wulkanowy.manager.server.repositories.BitriseRepository
import io.github.wulkanowy.manager.server.repositories.GithubRepository

class UnstableService(
    private val githubRepository: GithubRepository,
    private val bitriseRepository: BitriseRepository,
) {

    suspend fun getLatestBuilds(repoName: String, appId: String): ApiResponse<List<PullRequestBuild>> {
        val pullRequests = githubRepository.getPullRequests(repoName)

        val builds = pullRequests.mapNotNull {
            val build = bitriseRepository.getLastBuildsForBranch(appId, it.head.ref)
                .getOrNull(0) ?: return@mapNotNull null

            PullRequestBuild(
                id = it.id,
                title = it.title,
                number = it.number,
                buildTimestamp = build.finishedAt,
                githubUrl = it.htmlUrl,
                buildSlug = build.buildSlug,
                artifactSlug = build.artifactSlug,
                buildNumber = build.buildNumber,
                userAvatarUrl = it.user.avatarUrl,
                userLogin = it.user.login,
                commitSha = it.head.sha,
            )
        }

        return ApiResponse(success = true, data = builds)
    }
}
