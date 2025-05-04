plugins {
    id("common-conventions")
    id("spring-conventions")
    kotlin("jvm")
}

dependencies {
    api(libs.kotlin.reflect)
    api(libs.bundles.spring)
    api(libs.spring.cloud.starter.config)
    api(libs.gson)
    api(libs.jackson.module.kotlin)
}