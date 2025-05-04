plugins {
    org.jetbrains.kotlin.jvm
    me.champeau.jmh
}


repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}