val ktor_version: String by project
val koin_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
}

group = "io.github.wulkanowy"
version = "0.1.0"
application {
    mainClass.set("io.github.wulkanowy.manager.server.ApplicationKt")
}
task("stage") {
    dependsOn("installDist")
}

repositories {
    mavenCentral()
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = JavaVersion.VERSION_11.toString()
    freeCompilerArgs = listOf(*freeCompilerArgs.toTypedArray(), "-Xopt-in=kotlin.RequiresOptIn")
}
compileTestKotlin.kotlinOptions {
    jvmTarget = JavaVersion.VERSION_11.toString()
}

dependencies {
    // server
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    // client
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    // di
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}
