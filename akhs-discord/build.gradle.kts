import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `common-module`
    spring
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("net.dv8tion:JDA:5.0.0-beta.23")
    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    enabled = false
}

application {
    mainClass = "net.kingchev.discord.Launcher"
}

tasks.test {
    useJUnitPlatform()
}
