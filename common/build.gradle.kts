plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.GOOGLE_DEVTOOLS_KSP) version Versions.ksp
}

android {
    namespace = "com.unfinished.common"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MINIMUM_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

            buildConfigField("String", "WEBSITE_URL", "\"https://novawallet.io\"")
            buildConfigField("String", "PRIVACY_URL", "\"https://novawallet.io/privacy\"")
            buildConfigField("String", "TERMS_URL", "\"https://novawallet.io/terms\"")
            buildConfigField("String", "GITHUB_URL", "\"https://github.com/nova-wallet\"")
            buildConfigField("String", "TELEGRAM_URL", "\"https://t.me/novawallet\"")
            buildConfigField("String", "TWITTER_URL", "\"https://twitter.com/novawalletapp\"")
            buildConfigField("String", "RATE_URL", "\"market://details?id=com.unfinished.dsnp_wallet_kotlin.market\"")
            buildConfigField("String", "EMAIL", "\"support@novawallet.io\"")
            buildConfigField("String", "YOUTUBE_URL", "\"https://www.youtube.com/channel/UChoQr3YPETJKKVvhQ0AfV6A\"")
            buildConfigField("String", "TWITTER_ACCOUNT_TEMPLATE", "\"https://twitter.com/%s\"")
            buildConfigField("String", "RECOMMENDED_VALIDATORS_LEARN_MORE", "\"https://github.com/nova-wallet/nova-utils/wiki/Recommended-validators-in-Nova-Wallet\"")
            buildConfigField("String", "PAYOUTS_LEARN_MORE", "\"https://wiki.polkadot.network/docs/en/learn-simple-payouts\"")
            buildConfigField("String", "SET_CONTROLLER_LEARN_MORE", "\"https://wiki.polkadot.network/docs/en/maintain-guides-how-to-nominate-polkadot#setting-up-stash-and-controller-keys\"")
            buildConfigField("String", "PARITY_SIGNER_TROUBLESHOOTING", "\"https://github.com/nova-wallet/nova-utils/wiki/Parity-Signer-troubleshooting\"")
            buildConfigField("String", "LEDGER_BLEUTOOTH_GUIDE", "\"https://support.ledger.com/hc/en-us/articles/360019138694-Set-up-Bluetooth-connection\"")
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Modules.DATA))

    implementation(Dependencies.Support.core_ktx)
    implementation(Dependencies.Support.app_compat)
    implementation(Dependencies.Support.material)
    implementation(Dependencies.Support.constraint_layout)
    implementation(Dependencies.Support.flexbox)


    implementation(Dependencies.Lifecycle.extensions)
    implementation(Dependencies.Lifecycle.compiler)

    implementation(Dependencies.Extension.viewmodel_ktx)
    implementation(Dependencies.Extension.livedata_ktx)
    implementation(Dependencies.Extension.fragment_ktx)
    implementation(Dependencies.Extension.lifecycle_ktx)

    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)
    implementation(Dependencies.Coroutines.test)
    implementation(Dependencies.Coroutines.jdk8)

    implementation(Dependencies.DaggerHilt.hilt_android)
    kapt(Dependencies.DaggerHilt.android_compiler)
    kapt(Dependencies.DaggerHilt.hilt_compiler)

    implementation(Dependencies.Nova.fearless_utils)

    implementation(Dependencies.Coil.coil)
    implementation(Dependencies.Coil.coil_svg)

    implementation(Dependencies.Other.zxing)
    implementation(Dependencies.Other.zxing_android)
    implementation(Dependencies.Other.progressbutton)
    implementation(Dependencies.Other.shimmer)
    implementation(Dependencies.Other.insetter_widgets)
    implementation(Dependencies.Other.runtime_permission)

}