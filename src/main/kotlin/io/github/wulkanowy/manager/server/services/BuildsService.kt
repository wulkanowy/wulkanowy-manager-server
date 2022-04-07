package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.BitriseBuild
import io.github.wulkanowy.manager.server.repositories.BitriseRepository

class BuildsService(
    private val bitriseRepository: BitriseRepository,
) {

    suspend fun getLastBuildFromBranch(appId: String, branchName: String): ApiResponse<BitriseBuild> {
        val builds = bitriseRepository.getLastBuildsForBranch(appId, branchName)

        if (builds.isEmpty()) {
            return ApiResponse(success = false, error = "There are no successful builds for branch $branchName")
        }

        return ApiResponse(success = true, data = builds[0])
    }

    suspend fun getDownloadPageRedirect(appId: String, branchName: String): String {
        val builds = bitriseRepository.getBuildsByBranchName(appId, branchName, 1)
        val buildArtifact = bitriseRepository.getArtifactsByBuildSlug(appId, builds[0].slug, 1)[0]

        return getDownloadPageRedirect(appId, builds[0].slug, buildArtifact.slug)
    }

    suspend fun getDownloadPageRedirect(appId: String, buildId: String, artifactId: String): String {
        return bitriseRepository.getArtifactByArtifactSlug(appId, buildId, artifactId).publicInstallPageUrl
    }
}
