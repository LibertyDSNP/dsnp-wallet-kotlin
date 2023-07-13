import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    maven(url = "https://jitpack.io")
    maven(url = "https://plugins.gradle.org/m2/")
}