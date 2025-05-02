plugins {
    `common-module`
    org.jetbrains.kotlin.plugin.spring
    io.spring.`dependency-management`
    org.springframework.boot
    application
}

dependencies {
    implementation(libs.spring.cloud.config.server)
    implementation(libs.spring.boot.starter.actuator)
}

application {
    mainClass = "net.kingchev.configurations.Launcher"
}
