plugins {
    id("mpeix.android.lib")
    id("mpeix.android.compose")
}

dependencies {
    implementation(project(":common_app_lifecycle"))
    implementation(project(":common_di"))
    implementation(project(":library_elm"))
    implementation(project(":common_feature_toggles"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_navigation_api"))
    implementation(project(":common_navigation_compose"))
    implementation(project(":domain_app_settings"))
    implementation(project(":domain_app_settings_models"))

    implementation(project(":ext_koin"))

    implementation(project(":ui_kit_lists"))
    implementation(project(":ui_kit_switch"))
    implementation(project(":ui_kit_topappbar"))
    implementation(project(":ui_theme"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.appyx.core)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.compose.activity)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)
    implementation(libs.chucker.library)
}
