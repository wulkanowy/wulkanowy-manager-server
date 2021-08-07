package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitriseBuild(

    @SerialName("build_number")
    val buildNumber: Int,

    @SerialName("build_url")
    val buildUrl: String,

    @SerialName("commit_view_url")
    val commitViewUrl: String,

    @SerialName("expiring_download_url")
    val expiringDownloadUrl: String,

    @SerialName("file_size_bytes")
    val fileSizeBytes: Int,

    @SerialName("finished_at")
    val finishedAt: String,

    @SerialName("public_install_page_url")
    val publicInstallPageUrl: String
)
