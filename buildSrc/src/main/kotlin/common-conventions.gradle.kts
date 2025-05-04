@file:Suppress("UnstableApiUsage", "DEPRECATION")

import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugin.serialization
    org.jetbrains.kotlin.plugin.spring
    org.jetbrains.kotlin.plugin.allopen
}

group = "net.kingchev"
version = "0.0.1"

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    all {
        exclude("commons-logging", "commons-logging")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}