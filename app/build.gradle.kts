import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    id(Plugins.ANDROID_APPLICATION)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_PARCELIZE)
    id(Plugins.DAGGER_HILT_ANDROID_PLUGIN)
    id(Plugins.ANDROID_NAVIGATION_SAFE_ARGS)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.GOOGLE_DEVTOOLS_KSP) version Versions.ksp
}


android {
    namespace = "com.unfinished.dsnp_wallet_kotlin"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        applicationId = Config.APPLICATION_ID
        minSdk = Config.MINIMUM_SDK
        targetSdk = Config.TARGET_SDK
        versionCode = Config.VERSION_CODE
        versionName = Config.VERSION_NAME
        ndkVersion = Config.NDK_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(Config.DEEP_LINK_JUMP_TO_APP.type, Config.DEEP_LINK_JUMP_TO_APP.key, Config.DEEP_LINK_JUMP_TO_APP.value)
        manifestPlaceholders["deepLinkJumpToApp"] = Config.deepLinkJumpToApp.value
    }

    signingConfigs {
        create(Flavors.DEV) {
            storeFile = file("../dev.jks")
            storePassword = getLocalProperty("DEV_STORE_PASSWORD")
            keyAlias = getLocalProperty("DEV_KEY_ALIAS")
            keyPassword = getLocalProperty("DEV_KEY_PASSWORD")
        }
        create(Flavors.PROD) {
            storeFile = file("../prod.jks")
            storePassword = getLocalProperty("PROD_STORE_PASSWORD")
            keyAlias = getLocalProperty("PROD_KEY_ALIAS")
            keyPassword = getLocalProperty("PROD_KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName(BuildTypes.RELEASE) {
            isMinifyEnabled = BuildTypes.Release.isMinifyEnabled
            isShrinkResources = BuildTypes.Release.isShrinkResources
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName(BuildTypes.DEBUG) {
            signingConfig = null
            isDebuggable = BuildTypes.Debug.isDebuggable
        }
    }
    flavorDimensions += listOf(Flavors.dimension)

    productFlavors {
        create(Flavors.DEV) {
            dimension = Flavors.dimension
            applicationIdSuffix = Flavors.Dev.applicationIdSuffix
            versionNameSuffix = Flavors.Dev.versionNameSuffix
            signingConfig = signingConfigs.getByName(Flavors.DEV)

            buildConfigField(Flavors.Dev.WEB_URL.type, Flavors.Dev.WEB_URL.key, Flavors.Dev.WEB_URL.value)
            buildConfigField(Flavors.Dev.APP_URL.type, Flavors.Dev.APP_URL.key, Flavors.Dev.APP_URL.value)
            manifestPlaceholders["webUrl"] = Flavors.Dev.webUrl.value
            manifestPlaceholders["appUrl"] = Flavors.Dev.appUrl.value
        }
        create(Flavors.PROD) {
            dimension = Flavors.dimension
            signingConfig = signingConfigs.getByName(Flavors.PROD)

            buildConfigField(Flavors.Prod.WEB_URL.type, Flavors.Prod.WEB_URL.key, Flavors.Prod.WEB_URL.value)
            buildConfigField(Flavors.Prod.APP_URL.type, Flavors.Prod.APP_URL.key, Flavors.Prod.APP_URL.value)
            manifestPlaceholders["webUrl"] = Flavors.Prod.webUrl.value
            manifestPlaceholders["appUrl"] = Flavors.Prod.appUrl.value
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compilerExtension
    }
    project.configure<com.android.build.api.dsl.ApplicationExtension> {
        lint {
            abortOnError = true
            //baselineFile = file("lint-baseline.xml")
        }
    }
}

dependencies {
    implementation(project(Modules.Features.ACCOUNT))
    implementation(project(Modules.DATA))
    implementation(project(Modules.COMMON))
    implementation(project(Modules.RUNTIME))
    implementation(project(Modules.UIKIT))

    implementation(Dependencies.Support.core_ktx)
    implementation(Dependencies.Support.app_compat)
    implementation(Dependencies.Support.material)
    implementation(Dependencies.Support.constraint_layout)

    implementation(Dependencies.Test.junit)
    implementation(Dependencies.Test.ext_junit)
    implementation(Dependencies.Test.espresso_core)

    implementation(Dependencies.Navigation.navigation_fragment_ktx)
    implementation(Dependencies.Navigation.navigation_ui_ktx)

    implementation(Dependencies.Extension.viewmodel_ktx)
    implementation(Dependencies.Extension.livedata_ktx)
    implementation(Dependencies.Extension.fragment_ktx)

    implementation(Dependencies.Lifecycle.extensions)
    implementation(Dependencies.Lifecycle.compiler)

    implementation(Dependencies.DaggerHilt.hilt_android)
    kapt(Dependencies.DaggerHilt.android_compiler)
    kapt(Dependencies.DaggerHilt.hilt_compiler)

    implementation(Dependencies.Retrofit.okhttp)
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.converter_gson)
    implementation(Dependencies.Retrofit.converter_scalars)
    implementation(Dependencies.Retrofit.interceptor)

    implementation(Dependencies.Glide.glide)
    ksp(Dependencies.Glide.glide_compiler)

    implementation(Dependencies.Other.timber)
    implementation(Dependencies.Other.pincode_view)

    implementation(Dependencies.Coil.coil)
    implementation(Dependencies.Coil.coil_svg)

    implementation(Dependencies.Nova.websocket)
    implementation(Dependencies.Nova.fearless_utils)

    implementation(Dependencies.Compose.destinations)
    implementation(Dependencies.Compose.viewbinding)
    ksp(Dependencies.Compose.ksp)

    /**
     * Temp library until compose officially supports scrollbars
     */
    implementation(Dependencies.Compose.lazyColumnScrollbar)
}

fun getLocalProperty(key: String, file: String = "local.properties"): String? {
    val properties = Properties()
    val localProperties = File(file)
    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else error("File from not found")

    return properties.getProperty(key)
}