import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.helsinkiwizard.cointoss"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.helsinkiwizard.cointoss"
        minSdk = 26
        targetSdk = 35
        versionCode = 154
        versionName = "1.4.3"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("${rootProject.projectDir}/debug.keystore")
        }
    }

    buildTypes {
        getByName("debug") {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "false"
        }
        getByName("release") {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "true"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=com.google.android.horologist.tiles.ExperimentalHorologistTilesApi",
            "-opt-in=com.google.android.horologist.compose.tools.ExperimentalHorologistComposeToolsApi"
        )
    }
}

dependencies {
    implementation(project(":core"))

    implementation(libs.activity.compose)
    implementation(libs.lifecycle.process)
    implementation(libs.compose.runtime.livedata)

    implementation(libs.wear.remote.interactions)
    implementation(libs.wear.compose.material)
    implementation(libs.wear.compose.foundation)
    implementation(libs.wear.compose.navigation)
    implementation(libs.wear.tooling.preview)

    implementation(libs.horologist.tiles)
    implementation(libs.wear.tiles.material)
    implementation(libs.wear.protolayout)

    implementation(libs.horologist.compose.tools)
    implementation(libs.horologist.compose.layout)

    implementation(libs.accompanist.pager)
    implementation(libs.coil.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.leakcanary)
}
