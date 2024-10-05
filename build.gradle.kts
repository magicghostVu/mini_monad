import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("maven-publish")
}


repositories {
    mavenCentral()
}

dependencies {
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    //implementation("io.arrow-kt:arrow-core:1.2.4")
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
    /*repositories {
        maven {
            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value = project.properties.getValue("deploy.token") as String
            }
            url = uri("https://gitlab.zingplay.com/api/v4/projects/63/packages/maven")
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }*/

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/magicghostVu/mini_monad")
            credentials() {
                username = "magicghostVu"
                password = project.properties.getValue("github.deploy.token") as String
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {

            groupId = "org.magicghostvu"
            artifactId = "mini-monad"
            version = "0.1.5"

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
        }
    }
}

