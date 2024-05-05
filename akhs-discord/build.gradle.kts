dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.23")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
