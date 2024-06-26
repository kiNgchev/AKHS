import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":akhs-core"))
    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    mainClass = "net.kingchev.youtube.Launcher"
}


tasks.test {
    useJUnitPlatform()
}
