plugins {
    id("mpeix.feature.impl")
}

dependencies {
    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.appyx.core)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(project(":feature_main_screen_api"))
    implementation(project(":feature_dashboard_api"))
    implementation(project(":feature_schedule_api"))
    implementation(project(":feature_map_api"))
    implementation(project(":feature_bars_api"))
    implementation(project(":lib_navigation_compose"))
    implementation(project(":ui_icons"))
    implementation(project(":ui_kit_navigationbar"))
}
