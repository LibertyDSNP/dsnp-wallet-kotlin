plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.GOOGLE_DEVTOOLS_KSP) version Versions.ksp
}

android {
    namespace = "com.unfinished.data"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MINIMUM_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        ksp {
//            arg("room.schemaLocation", "$projectDir/schemas")
//        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName(BuildTypes.RELEASE) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation(Dependencies.Support.core_ktx)
    implementation(Dependencies.Support.app_compat)
    implementation(Dependencies.Support.material)

    implementation(Dependencies.DaggerHilt.hilt_android)
    kapt(Dependencies.DaggerHilt.android_compiler)
    kapt(Dependencies.DaggerHilt.hilt_compiler)

    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.ktx)
    kapt(Dependencies.Room.compiler)
    implementation(Dependencies.Room.testing)

    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)

    implementation(Dependencies.Nova.bouncycastle)
    implementation(Dependencies.Nova.fearless_utils)
}