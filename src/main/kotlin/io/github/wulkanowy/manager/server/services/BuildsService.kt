package io.github.wulkanowy.manager.server.services

import io.github.wulkanowy.manager.server.models.ApiResponse
import io.github.wulkanowy.manager.server.models.BitriseBuild
import io.github.wulkanowy.manager.server.repositories.BitriseRepository

class BuildsService(
    private val bitriseRepository: BitriseRepository,
) {

    suspend fun getLastBuildFromBranch(appId: String, branchName: String): ApiResponse<BitriseBuild> {
        val builds = bitriseRepository.getBuildsByBranchName(appId, branchName, 1)
        if (builds.isEmpty()) {
            return ApiResponse(success = false, error = "There is no successful builds for branch $branchName")
        }

        val buildArtifact = bitriseRepository.getArtifactsByBuildSlug(appId, builds[0].slug, 1)[0]
        val info = bitriseRepository.getArtifactByArtifactSlug(appId, builds[0].slug, buildArtifact.slug)
        val bitriseBuild = BitriseBuild(
            buildNumber = builds[0].buildNumber,
            buildUrl = "https://app.bitrise.io/build/" + buildArtifact.slug,
            commitViewUrl = builds[0].commitViewUrl,
            expiringDownloadUrl = info.expiringDownloadUrl,
            fileSizeBytes = info.fileSizeBytes,
            finishedAt = builds[0].finishedAt,
            publicInstallPageUrl = info.publicInstallPageUrl,
        )

        return ApiResponse(success = true, data = bitriseBuild)
    }
}
