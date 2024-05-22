import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("maven-publish")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
}

tasks.withType<KotlinCompile>(){
    kotlinOptions.freeCompilerArgs = listOf(
        "-Xno-call-assertions",
        "-Xno-receiver-assertions",
        "-Xno-param-assertions"
    )
}


kotlin {
    jvmToolchain(8)
}

java {
    withSourcesJar()
    //withJavadocJar()
}

publishing {
    repositories {
        maven {
            credentials {
                username = "phuvh"
                password = "VmlAC8AFRAsnUDB23f2YfWqZu6CM59qUOgox1h14a1GDdr5rNrNqPfLJnRNfkz6V"
            }
            url = uri("http://localhost:3001/releases")
            isAllowInsecureProtocol = true
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {

            groupId = "org.magicghostvu"
            artifactId = "mini-monad"
            version = "0.1-SNAPSHOT"

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
        }
    }
}

