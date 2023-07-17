apply{
    from("versions.gradle")
}
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Plugins.android_gradle_plugin)
        classpath(Plugins.hilt_gradle_plugin)
        classpath(Plugins.args_gradle_plugin)
        classpath(Plugins.mozila_rust_gradle_plugin)
        classpath(Plugins.kotlin_gradle_plugin)
    }
}

task("clean") {
    delete(project.buildDir)
}