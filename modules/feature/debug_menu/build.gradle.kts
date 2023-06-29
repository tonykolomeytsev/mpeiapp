plugins {
    id("mpeix.android.lib")
    id("mpeix.android.compose")
}

dependencies {
    implementation(project(":ui_theme"))
    implementation(project(":common_elm"))
    implementation(project(":common_navigation_api"))
    implementation(project(":common_navigation_compose"))
    implementation(project(":domain_app_settings_models"))
    implementation(project(":common_kotlin"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.appyx.core)
    implementation(libs.koin.core)
    implementation(libs.compose.activity)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)
}
