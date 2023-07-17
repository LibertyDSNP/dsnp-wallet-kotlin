
object Plugins {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"
    const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
    const val ANDROID = "android"
    const val KOTLIN_KAPT = "kotlin-kapt"
    const val KOTLIN_PARCELIZE = "kotlin-parcelize"
    const val DAGGER_HILT_ANDROID_PLUGIN = "dagger.hilt.android.plugin"
    const val ANDROID_NAVIGATION_SAFE_ARGS = "androidx.navigation.safeargs"
    const val GOOGLE_DEVTOOLS_KSP = "com.google.devtools.ksp"

    const val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    const val hilt_gradle_plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_gradle_plugin}"
    const val args_gradle_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.args_gradle_plugin}"
    const val mozila_rust_gradle_plugin = "org.mozilla.rust-android-gradle:plugin:${Versions.mozila_rust_gradle_plugin}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}