plugins {
    id("mpeix.feature.impl")
}

@Suppress("UnstableApiUsage")
android.buildFeatures {
    viewBinding = true
    androidResources = true
}

dependencies {
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.viewPager2)
    implementation(libs.appyx.core)
    implementation(libs.facebook.shimmer)
    implementation(libs.firebase.crashlytics)
    implementation(libs.google.gson)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)

    implementation(project(":feature_app_settings_api"))
    implementation(project(":feature_main_screen_api"))
    implementation(project(":feature_notes_api"))
    implementation(project(":feature_onboarding_api"))
    implementation(project(":feature_schedule_api"))

    implementation(project(":lib_adapter"))
    implementation(project(":lib_analytics_api"))
    implementation(project(":lib_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":lib_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":lib_elm"))
    implementation(project(":lib_navigation"))
    implementation(project(":lib_navigation_compose"))
    implementation(project(":lib_network"))
    implementation(project(":lib_schedule"))
    implementation(project(":lib_persistent_cache"))

    implementation(project(":ui_theme"))

    implementation(project(":coreui"))
    implementation(project(":res_strings"))
}
