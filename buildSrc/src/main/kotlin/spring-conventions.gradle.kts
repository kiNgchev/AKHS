import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugin.spring
    io.spring.`dependency-management`
    org.springframework.boot
    org.hibernate
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}