package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitriseArtifactInfoData(
    @SerialName("artifact_meta")
    val artifactMeta: ArtifactMeta,
    @SerialName("artifact_type")
    val artifactType: String,
    @SerialName("expiring_download_url")
    val expiringDownloadUrl: String,
    @SerialName("file_size_bytes")
    val fileSizeBytes: Int,
    @SerialName("is_public_page_enabled")
    val isPublicPageEnabled: Boolean,
    @SerialName("public_install_page_url")
    val publicInstallPageUrl: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("title")
    val title: String
)
