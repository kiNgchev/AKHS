plugins {
    id("common-conventions")
    id("spring-conventions")
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("org.telegram:telegrambots-longpolling:8.3.0")
    implementation("org.telegram:telegrambots-springboot-longpolling-starter:8.3.0")
    implementation("org.telegram:telegrambots-client:8.3.0")
}

application {
    mainClass = "net.kingchev.telegram.Launcher"
}
