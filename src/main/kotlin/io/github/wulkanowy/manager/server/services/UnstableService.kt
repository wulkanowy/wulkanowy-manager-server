package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.repositories.GithubRepository

class UnstableService(
    private val githubRepository: GithubRepository,
) {

    suspend fun getLatestBuilds() = githubRepository.getPullRequests()
}
