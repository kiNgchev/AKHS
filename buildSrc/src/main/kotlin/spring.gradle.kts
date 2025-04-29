import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.withType
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    org.jetbrains.kotlin.jvm
    io.spring.`dependency-management`
    org.springframework.boot
    org.hibernate
}

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.spring)
}

tasks.withType<BootJar> {
    enabled = false
}