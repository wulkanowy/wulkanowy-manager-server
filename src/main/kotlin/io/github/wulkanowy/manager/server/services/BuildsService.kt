package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.BitriseArtifactInfoData
import io.github.wulkanowy.manager.server.repositories.BitriseRepository

class BuildsService(
    private val bitriseRepository: BitriseRepository,
) {

    suspend fun getLastBuildFromBranch(appId: String, branchName: String): ApiResponse<BitriseArtifactInfoData> {
        val builds = bitriseRepository.getBuildsByBranchName(appId, branchName, 1)
        if (builds.isEmpty()) {
            return ApiResponse(success = false, error = "There is no successful builds for branch $branchName")
        }

        val buildArtifacts = bitriseRepository.getArtifactsByBuildSlug(appId, builds[0].slug, 1)[0]
        val info = bitriseRepository.getArtifactByArtifactSlug(appId, builds[0].slug, buildArtifacts.slug)

        return ApiResponse(success = true, data = info)
    }
}
