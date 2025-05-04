plugins {
    id("common-conventions")
    id("spring-conventions")
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("org.telegram:telegrambots:6.9.7.1")
    implementation("org.telegram:telegrambots-abilities:6.9.7.1")
}

application {
    mainClass = "net.kingchev.telegram.Launcher"
}
