plugins {
    `common-module`
    spring
    application
}

dependencies {
    implementation(project(":akhs-core"))
}

application {
    mainClass = "net.kingchev.youtube.Launcher"
}
