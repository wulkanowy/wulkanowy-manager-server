package io.github.wulkanowy.manager.server.repositories

import io.github.wulkanowy.manager.server.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class BitriseRepository(
    private val client: HttpClient
) {
    companion object {
        private const val BITRISE_BASE = "https://api.bitrise.io/v0.1/apps"
    }

    suspend fun getLastBuildsForBranch(appId: String, branchName: String): List<BitriseBuild> {
        val builds = getBuildsByBranchName(appId, branchName, 1)
        if (builds.isEmpty()) {
            return emptyList()
        }

        return getArtifactsByBuildSlug(appId, builds[0].slug, 1).map {
            BitriseBuild(
                buildNumber = builds[0].buildNumber,
                buildSlug = builds[0].slug,
                artifactSlug = it.slug,
                commitViewUrl = builds[0].commitViewUrl,
                finishedAt = builds[0].finishedAt,
            )
        }
    }

    /**
     * @see [https://api-docs.bitrise.io/#/builds/build-list]
     */
    suspend fun getBuildsByBranchName(appId: String, branchName: String, limit: Int): List<BitriseBuildItem> {
        return client.get("$BITRISE_BASE/$appId/builds") {
            parameter("branch", branchName)
            parameter("status", 1)
            parameter("limit", limit)
        }.body<BitriseResponse<List<BitriseBuildItem>>>().data!!
    }

    /**
     * @see [https://api-docs.bitrise.io/#/build-artifact/artifact-list]]
     */
    suspend fun getArtifactsByBuildSlug(appId: String, buildSlug: String, limit: Int): List<BuildArtifactData> {
        return client.get("$BITRISE_BASE/$appId/builds/$buildSlug/artifacts") {
            parameter("limit", limit)
        }.body<BitriseResponse<List<BuildArtifactData>>>().data!!
    }

    /**
     * @see [https://api-docs.bitrise.io/#/build-artifact/artifact-show]
     */
    suspend fun getArtifactByArtifactSlug(
        appId: String, buildSlug: String, artifactSlug: String
    ): BitriseArtifactInfoData {
        val response = client.get("$BITRISE_BASE/$appId/builds/$buildSlug/artifacts/$artifactSlug")

        if (response.status == HttpStatusCode.Unauthorized) {
            throw Exception(response.status.description)
        }

        return response.body<BitriseResponse<BitriseArtifactInfoData>>().data!!
    }
}
