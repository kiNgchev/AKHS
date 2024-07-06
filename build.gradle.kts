import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

buildscript {
    val kotlinVersion = "1.8.20"
    val kotlinExtVersion = "2.0.0-Beta2"
    val springBootVersion = "3.2.2"
    val hibernateVersion = "6.4.1.Final"
    val author = "kiNgchev"

    repositories {
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0-Beta4")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
        classpath("org.hibernate.orm:hibernate-gradle-plugin:${hibernateVersion}")
        classpath("org.jetbrains.kotlin:kotlin-noarg:${kotlinExtVersion}")
    }
}

plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

allprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    repositories {
        mavenCentral()
    }

    group = "net.kingchev"
    version = "0.0.1-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain(21)
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }

        allOpen {
            annotations(
                "javax.persistence.Entity",
                "javax.persistence.MappedSuperclass",
                "javax.persistence.Embedabble"
            )
        }

        all {
            exclude("commons-logging", "commons-logging")
        }
    }

    dependencies {
        //Okhttp
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        //Gson
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.springframework.kafka:spring-kafka")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.liquibase:liquibase-core")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        implementation("org.springframework.boot:spring-boot-docker-compose")
        runtimeOnly("org.postgresql:postgresql")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.kafka:spring-kafka-test")

    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

tasks.withType<BootJar> {
    enabled = false
}

repositories {
    mavenCentral()
}
