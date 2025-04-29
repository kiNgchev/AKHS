import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    org.jetbrains.kotlin.jvm
    me.champeau.jmh
}

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jmh.core)
    implementation(libs.jmh.generator.annprocess)
    implementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}