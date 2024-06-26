import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":akhs-core"))
    implementation("net.dv8tion:JDA:5.0.0-beta.23")
    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    mainClass = "net.kingchev.discord.Launcher"
}

tasks.test {
    useJUnitPlatform()
}
