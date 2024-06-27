import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    application
}

dependencies {
    implementation(project(":akhs-core"))
    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    enabled = false
}

application {
    mainClass = "net.kingchev.youtube.Launcher"
}

tasks.test {
    useJUnitPlatform()
}
