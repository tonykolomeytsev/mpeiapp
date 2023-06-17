buildscript {
    dependencies {
        // specify android gradle plugin version in versions catalog
        classpath(libs.agp)
    }
}

plugins {
    // `apply false` in root project is just the way
    // to specify plugin versions for all subprojects
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.gradle.android.cacheFix) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false

    id("mpeix.android-jar-finder")
    id("mpeix.android.lib") apply false
    id("mpeix.kotlin.lib") apply false
    id("mpeix.ksp") apply false
}
