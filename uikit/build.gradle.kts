plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
}

android {
    namespace = "com.unfinished.uikit"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MINIMUM_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compilerExtension
    }
}

dependencies {
    implementation(Dependencies.Support.core_ktx)
    implementation(Dependencies.Support.app_compat)
    implementation(Dependencies.Support.material)

    platform(Dependencies.Compose.bom)
    api(Dependencies.Compose.material3)
    api(Dependencies.Compose.material2)
    api(Dependencies.Compose.foundation)
    api(Dependencies.Compose.ui)
    api(Dependencies.Compose.preview)
    api(Dependencies.Compose.ui_tooling)
    api(Dependencies.Compose.junit)
    api(Dependencies.Compose.ui_test)
    api(Dependencies.Compose.window_size)
    api(Dependencies.Compose.activity)
    api(Dependencies.Compose.viewmodel)
    api(Dependencies.Compose.hilt)
    api(Dependencies.Support.chrome_tabs)
    api(Dependencies.Coil.coil_compose)
}