package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.BitriseArtifactInfoData
import io.github.wulkanowy.manager.server.repositories.BitriseRepository

class BuildsService(
    private val bitriseRepository: BitriseRepository,
) {

    suspend fun getLastBuildFromBranch(appId: String, branchName: String): ApiResponse<BitriseArtifactInfoData> {
        val build = bitriseRepository.getBuildsByBranchName(appId, branchName, 1)[0]
        val buildArtifacts = bitriseRepository.getArtifactsByBuildSlug(appId, build.slug, 1)[0]
        val info = bitriseRepository.getArtifactByArtifactSlug(appId, build.slug, buildArtifacts.slug)

        return ApiResponse(success = true, data = info)
    }
}
