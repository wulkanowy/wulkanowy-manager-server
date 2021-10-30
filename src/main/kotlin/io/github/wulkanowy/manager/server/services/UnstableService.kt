package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.PullRequestBuild
import io.github.wulkanowy.manager.server.repositories.GithubRepository

class UnstableService(
    private val githubRepository: GithubRepository,
) {

    suspend fun getLatestBuilds(): ApiResponse<List<PullRequestBuild>> {
        val pullRequests = githubRepository.getPullRequests()

        return ApiResponse(success = true, data = pullRequests)
    }
}
