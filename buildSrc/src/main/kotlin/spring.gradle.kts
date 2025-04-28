import gradle.kotlin.dsl.accessors._96aa118a43afcb4b016ffc817e61dbf0.implementation
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
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