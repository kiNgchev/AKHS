import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `common-module`
    spring
    application
}

dependencies {
    implementation(project(":akhs-core"))
    implementation("com.github.twitch4j:twitch4j:1.20.0")
    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    enabled = false
}

application {
    mainClass = "net.kingchev.twitch.Launcher"
}


tasks.test {
    useJUnitPlatform()
}
