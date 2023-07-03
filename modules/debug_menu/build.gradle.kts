plugins {
    id("mpeix.feature.impl")
}

@Suppress("UnstableApiUsage")
android.buildFeatures.androidResources = true

dependencies {
    implementation(project(":library_app_lifecycle"))
    implementation(project(":library_app_info"))
    implementation(project(":library_elm"))
    implementation(project(":library_feature_toggles"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_navigation_api"))
    implementation(project(":library_navigation_compose"))
    implementation(project(":library_network"))
    implementation(project(":feature_app_settings_api"))

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
