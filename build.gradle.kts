// Top-level build file where you can add configuration options common to all sub-projects/modules.

// AGP 9 has built-in Kotlin support and bundles KGP 2.2.10. Override to 2.3.20 to keep our
// Kotlin/KSP (2.3.10) pairing; the standalone kotlin-android plugin is no longer applied.
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
