package io.github.wulkanowy.manager.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildArtifactData(
    @SerialName("artifact_meta")
    val artifactMeta: ArtifactMeta,
    @SerialName("artifact_type")
    val artifactType: String,
    @SerialName("file_size_bytes")
    val fileSizeBytes: Int,
    @SerialName("is_public_page_enabled")
    val isPublicPageEnabled: Boolean,
    @SerialName("slug")
    val slug: String,
    @SerialName("title")
    val title: String
)

@Serializable
data class ArtifactMeta(
    @SerialName("aab")
    val aab: String,
    @SerialName("apk")
    val apk: String,
    @SerialName("app_info")
    val appInfo: AppInfo,
    @SerialName("build_type")
    val buildType: String,
    @SerialName("file_size_bytes")
    val fileSizeBytes: String,
//    @SerialName("include")
//    val include: Any?,
    @SerialName("info_type_id")
    val infoTypeId: String,
    @SerialName("module")
    val module: String,
    @SerialName("product_flavour")
    val productFlavour: String,
//    @SerialName("split")
//    val split: Any?,
    @SerialName("universal")
    val universal: String
)

@Serializable
data class AppInfo(
    @SerialName("app_name")
    val appName: String,
    @SerialName("min_sdk_version")
    val minSdkVersion: String,
    @SerialName("package_name")
    val packageName: String,
    @SerialName("version_code")
    val versionCode: String,
    @SerialName("version_name")
    val versionName: String
)
