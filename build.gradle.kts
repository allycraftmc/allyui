plugins {
    id("java-library")
    id("maven-publish")
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

java {
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            url = uri("https://mvn.allycraft.de/snapshots/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }

    publications {
        var gitCommit = System.getenv("GIT_COMMIT")
        if(gitCommit != null) {
            create<MavenPublication>("maven") {
                version = gitCommit

                from(components["java"])
            }
        }

        create<MavenPublication>("mavenSnapshot") {
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}