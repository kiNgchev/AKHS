plugins {
    id("common-conventions")
    org.jetbrains.kotlin.plugin.spring
    io.spring.`dependency-management`
    org.springframework.boot
    application
}

dependencies {
    implementation(libs.spring.cloud.config.server)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.logback.loki)
    implementation(libs.janino)
}

application {
    mainClass = "net.kingchev.configurations.LauncherKt"
}
