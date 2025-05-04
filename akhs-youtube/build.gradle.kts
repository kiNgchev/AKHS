plugins {
    id("common-conventions")
    id("spring-conventions")
    application
}

dependencies {
    implementation(project(":akhs-core"))
}

application {
    mainClass = "net.kingchev.youtube.Launcher"
}
