import gradle.kotlin.dsl.accessors._96aa118a43afcb4b016ffc817e61dbf0.implementation
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
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