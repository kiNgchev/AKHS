plugins {
    id("common-conventions")
    id("spring-conventions")
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("com.github.twitch4j:twitch4j:1.20.0")
}

application {
    mainClass = "net.kingchev.twitch.Launcher"
}
