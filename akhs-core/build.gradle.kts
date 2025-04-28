import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `common-module`
    spring
    kotlin("jvm")
}

group = "net.kingchev"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}