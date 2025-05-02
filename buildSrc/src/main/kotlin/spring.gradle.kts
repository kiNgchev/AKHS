import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.withType
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugin.spring
    io.spring.`dependency-management`
    org.springframework.boot
    org.hibernate
}

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation(libs.bundles.spring)
    implementation(libs.bundles.spring.cloud)
}

tasks.withType<BootJar> {
    enabled = false
}