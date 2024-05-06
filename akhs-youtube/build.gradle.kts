dependencies {
    implementation(project(":akhs-core"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
