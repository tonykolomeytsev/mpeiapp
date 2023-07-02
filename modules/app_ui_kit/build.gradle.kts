plugins {
    id("mpeix.android.application")
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
    implementation(project(":ui_icons"))
    implementation(project(":ui_kit_lists"))
    implementation(project(":ui_kit_topappbar"))
    implementation(project(":common_navigation_api"))
    implementation(project(":common_navigation_compose"))
    implementation(project(":common_elm"))
    implementation(project(":common_kotlin"))

    implementation(libs.compose.activity)
    implementation(libs.appyx.core)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
