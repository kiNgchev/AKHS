plugins {
    id("common-conventions")
    id("spring-conventions")
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("net.dv8tion:JDA:5.5.1")
}

application {
    mainClass = "net.kingchev.discord.Launcher"
}
