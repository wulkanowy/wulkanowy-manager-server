package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.PullRequestBuild
import io.github.wulkanowy.manager.server.repositories.GithubRepository

class UnstableService(
    private val githubRepository: GithubRepository,
) {

    suspend fun getLatestBuilds(): ApiResponse<List<PullRequestBuild>> {
        return try {
            ApiResponse(success = true, data = githubRepository.getPullRequests())
        } catch (e: Throwable) {
            ApiResponse(success = false, error = e.message, data = null)
        }
    }
}
