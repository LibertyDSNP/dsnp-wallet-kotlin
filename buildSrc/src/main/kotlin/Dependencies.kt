import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

sealed class Dependencies {
    object Support {
        val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
        val app_compat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        val material = "com.google.android.material:material:${Versions.material}"
        val constraint_layout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
        val biometric = "androidx.biometric:biometric:${Versions.biometric}"
        val flexbox = "com.google.android.flexbox:flexbox:${Versions.flexbox}"
        val chrome_tabs = "androidx.browser:browser:${Versions.chrome_tabs}"
    }

    object Test {
        val junit = "junit:junit:${Versions.junit}"
        val ext_junit = "androidx.test.ext:junit:${Versions.ext_junit}"
        val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
        val mockito_inline = "org.mockito:mockito-inline:${Versions.mockito_inline}"
        val runner = "androidx.test:runner:${Versions.runner}"
        val rules = "androidx.test:rules:${Versions.rules}"
        val hilt_testing = "com.google.dagger:hilt-android-testing:${Versions.hilt_testing}"
        val hilt_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt_compiler}"
    }

    object Navigation {
        val navigation_fragment_ktx =
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigation_fragment_ktx}"
        val navigation_ui_ktx =
            "androidx.navigation:navigation-ui-ktx:${Versions.navigation_ui_ktx}"
    }

    object Extension {
        val viewmodel_ktx =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodel_ktx}"
        val livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.livedata_ktx}"
        val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment_ktx}"
        val lifecycle_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle_ktx}"
    }

    object Lifecycle {
        val extensions =
            "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle_extensions}"
        val compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle_compiler}"
    }

    object Coroutines {
        val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines_core}"
        val android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_android}"
        val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_test}"
        val jdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Versions.coroutines_jdk8}"
    }

    object Room {
        val runtime = "androidx.room:room-runtime:${Versions.runtime}"
        val ktx = "androidx.room:room-ktx:${Versions.ktx}"
        val compiler = "androidx.room:room-compiler:${Versions.compiler}"
        val testing = "androidx.room:room-testing:${Versions.testing}"
    }

    object DaggerHilt {
        val hilt_android = "com.google.dagger:hilt-android:${Versions.hilt_android}"
        val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.viewmodel}"
        val android_compiler =
            "com.google.dagger:hilt-android-compiler:${Versions.android_compiler}"
        val hilt_compiler = "androidx.hilt:hilt-compiler:${Versions.hilt_compiler}"
    }

    object Retrofit {
        val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.converter_gson}"
        val converter_scalars =
            "com.squareup.retrofit2:converter-scalars:${Versions.converter_scalars}"
        val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
    }

    object Glide {
        val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide_compiler}"
    }

    object Coil {
        val coil = "io.coil-kt:coil:${Versions.coil}"
        val coil_svg = "io.coil-kt:coil-svg:${Versions.coil_svg}"
        val coil_compose = "io.coil-kt:coil-compose:${Versions.coil_compose}"
    }

    object Nova {
        val fearless_utils =
            "io.github.nova-wallet:substrate-sdk-android:${Versions.fearless_utils}"
        val nova_android_sdk =
            "com.github.LibertyDSNP:substrate-sdk-android:${Versions.nova_android_sdk}"
        val bouncycastle = "org.bouncycastle:bcprov-jdk15on:${Versions.bouncdycastle}"
        val websocket = "com.neovisionaries:nv-websocket-client:${Versions.websocket}"
    }

    object Other {
        val zxing = "com.google.zxing:core:${Versions.zxing}"
        val zxing_android = "com.journeyapps:zxing-android-embedded:${Versions.zxing_android}"
        val progressbutton =
            "com.github.razir.progressbutton:progressbutton:${Versions.progressbutton}"
        val insetter_widgets =
            "dev.chrisbanes.insetter:insetter-widgets:${Versions.insetter_widgets}"
        val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"
        val runtime_permission =
            "com.github.florent37:runtime-permission-kotlin:${Versions.runtime_permission}"
        val streamsupport =
            "net.sourceforge.streamsupport:streamsupport:${Versions.streamsupport}"

        val timber = "com.jakewharton.timber:timber:${Versions.timber}"
        val pincode_view = "io.github.chaosleung:pinview:${Versions.pincode_view}"
    }

    object Compose {
        val bom = "androidx.compose:compose-bom:${Versions.compose_bom}"
        val material3 = "androidx.compose.material3:material3"
        val material2 = "androidx.compose.material:material"
        val foundation = "androidx.compose.foundation:foundation"
        val ui = "androidx.compose.ui:ui"
        val preview = "androidx.compose.ui:ui-tooling-preview"
        val ui_tooling = "androidx.compose.ui:ui-tooling"
        val junit = "androidx.compose.ui:ui-test-junit4"
        val ui_test = "androidx.compose.ui:ui-test-manifest"
        val window_size = "androidx.compose.material3:material3-window-size-class"
        val activity = "androidx.activity:activity-compose:${Versions.compose_activity}"
        val viewmodel =
            "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.compose_viewmodel}"
        val hilt = "androidx.hilt:hilt-navigation-compose:${Versions.compose_hilt}"
        val destinations =
            "io.github.raamcosta.compose-destinations:animations-core:${Versions.compose_destinations}"
        val ksp =
            "io.github.raamcosta.compose-destinations:ksp:${Versions.compose_destinations}"
        val viewbinding = "androidx.compose.ui:ui-viewbinding:${Versions.compose_viewbinding}"
    }
}