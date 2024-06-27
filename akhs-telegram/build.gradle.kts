import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("org.telegram:telegrambots:6.9.7.1")
    implementation("org.telegram:telegrambots-abilities:6.9.7.1")
    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    enabled = false
}

application {
    mainClass = "net.kingchev.telegram.Launcher"
}

tasks.test {
    useJUnitPlatform()
}
