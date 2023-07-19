plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.GOOGLE_DEVTOOLS_KSP) version Versions.ksp
}

android {
    namespace = "com.unfinished.runtime"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MINIMUM_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CHAINS_URL", "\"https://raw.githubusercontent.com/DannyAhmedApex/nova-utils/master/chains/v5/chains.json\"")
        buildConfigField("String", "TEST_CHAINS_URL", "\"https://raw.githubusercontent.com/DannyAhmedApex/nova-utils/master/tests/chains_for_testBalance.json\"")
    }

    buildTypes {
        getByName(BuildTypes.RELEASE) {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "CHAINS_URL", "\"https://raw.githubusercontent.com/DannyAhmedApex/nova-utils/master/chains/v5/chains.json\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Modules.DATA))

    implementation(Dependencies.Lifecycle.extensions)

    implementation(Dependencies.Coroutines.core)

    implementation(Dependencies.Retrofit.okhttp)
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.converter_gson)
    implementation(Dependencies.Retrofit.converter_scalars)
    implementation(Dependencies.Retrofit.interceptor)

    implementation(Dependencies.DaggerHilt.hilt_android)
    kapt(Dependencies.DaggerHilt.android_compiler)
    kapt(Dependencies.DaggerHilt.hilt_compiler)

    implementation(Dependencies.Nova.bouncycastle)
    implementation(Dependencies.Nova.fearless_utils)
    implementation(Dependencies.Nova.websocket)

    //There is a part of code that needs this to support for under api 24
    implementation(Dependencies.Other.streamsupport)

}