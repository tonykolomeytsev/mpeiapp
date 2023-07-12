plugins {
    id("mpeix.feature.impl")
}

@Suppress("UnstableApiUsage")
android.buildFeatures {
    viewBinding = true
    androidResources = true
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.appyx.core)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.rx.java)
    implementation(libs.tinkoff.pagerIndicator)

    implementation(project(":feature_main_screen_api"))
    implementation(project(":feature_onboarding_api"))
    implementation(project(":feature_schedule_api"))

    implementation(project(":ext_android"))
    implementation(project(":ext_kotlin"))
    implementation(project(":lib_adapter"))
    implementation(project(":lib_analytics_android"))
    implementation(project(":lib_app_info"))
    implementation(project(":lib_elm"))
    implementation(project(":lib_navigation"))
    implementation(project(":lib_navigation_compose"))
    implementation(project(":lib_network"))

    implementation(project(":ui_theme"))

    implementation(project(":coreui"))
    implementation(project(":res_strings"))
}
