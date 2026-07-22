import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.helsinkiwizard.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    api(platform(libs.compose.bom))

    api(libs.core.ktx)
    api(libs.datastore.preferences)
    api(libs.play.services.wearable)
    api(libs.core.splashscreen)
    api(libs.timber)
    api(libs.compose.material.icons.core)
    api(libs.compose.material.icons.extended)

    implementation(libs.compose.foundation)
    api(libs.compose.ui.tooling.preview)

    api(platform(libs.firebase.bom))
    api(libs.firebase.crashlytics)
    api(libs.firebase.analytics)
    api(libs.firebase.perf)
}
