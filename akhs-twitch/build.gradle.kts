dependencies {
    implementation(project(":akhs-core"))
    implementation("com.github.twitch4j:twitch4j:1.20.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
