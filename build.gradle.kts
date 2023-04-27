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
    alias(libs.plugins.detekt)

    id("mpeix.android-jar-finder")
    id("mpeix.android") apply false
    id("mpeix.kotlin") apply false
}

println("Configure root project")

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }
    detekt {
        config = rootProject.files("detekt.yaml")
    }
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
    }
    tasks.withType<Delete> {
        delete(rootProject.buildDir)
    }
}
