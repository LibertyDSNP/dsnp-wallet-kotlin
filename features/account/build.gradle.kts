plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.KOTLIN_PARCELIZE)
    id(Plugins.DAGGER_HILT_ANDROID_PLUGIN)
}

android {
    namespace = "com.unfinished.account"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MINIMUM_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(Modules.DATA))
    implementation(project(Modules.COMMON))

    implementation(Dependencies.Support.core_ktx)
    implementation(Dependencies.Support.app_compat)
    implementation(Dependencies.Support.material)
    implementation(Dependencies.Support.constraint_layout)
    implementation(Dependencies.Support.biometric)

    implementation(Dependencies.DaggerHilt.hilt_android)
    kapt(Dependencies.DaggerHilt.android_compiler)
    kapt(Dependencies.DaggerHilt.hilt_compiler)

    androidTestImplementation(Dependencies.Test.hilt_testing)
    kaptAndroidTest(Dependencies.Test.hilt_compiler)

    implementation(Dependencies.Retrofit.converter_gson)

    implementation(Dependencies.Extension.viewmodel_ktx)
    implementation(Dependencies.Extension.livedata_ktx)
    implementation(Dependencies.Extension.fragment_ktx)
    implementation(Dependencies.Extension.lifecycle_ktx)

    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)

    implementation(Dependencies.Nova.bouncycastle)
    implementation(Dependencies.Nova.fearless_utils)
    implementation(Dependencies.Nova.websocket)

    implementation(Dependencies.Navigation.navigation_fragment_ktx)
    implementation(Dependencies.Navigation.navigation_ui_ktx)

    implementation(Dependencies.Coil.coil)
    implementation(Dependencies.Coil.coil_svg)

    implementation(Dependencies.Other.zxing)
    implementation(Dependencies.Other.zxing_android)

    implementation(Dependencies.Test.runner)
    implementation(Dependencies.Test.rules)
    implementation(Dependencies.Test.ext_junit)
}