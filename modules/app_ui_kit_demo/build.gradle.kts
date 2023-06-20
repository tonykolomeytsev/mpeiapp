plugins {
    id("com.android.application")
    id("mpeix.android.base")
    id("mpeix.android.compose")
}

android {
    namespace = "kekmech.ru.mpeiapp.demo"

    defaultConfig {
        applicationId = "kekmech.ru.mpeiapp.demo"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

dependencies {
    implementation(project(":ui_theme"))
    implementation(project(":common_navigation_api"))
    implementation(project(":common_navigation_compose"))

    implementation(libs.compose.activity)
    implementation(libs.appyx.core)
}
