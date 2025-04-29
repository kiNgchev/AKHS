plugins {
    `common-module`
    spring
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("net.dv8tion:JDA:5.0.0-beta.23")
}

application {
    mainClass = "net.kingchev.discord.Launcher"
}
