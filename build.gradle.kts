plugins {
    id("java-library")
}

group = "de.allycraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("net.minestom:minestom:2025.08.12-1.21.8")

    // demo
    testImplementation("net.minestom:minestom:2025.08.12-1.21.8")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
}

tasks.register<JavaExec>("runDemo") {
    mainClass.set("de.allycraft.minestom.ui.demo.DemoServer")
    classpath = sourceSets["test"].runtimeClasspath
}