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
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.java)
    implementation(libs.tinkoff.pagerIndicator)

    implementation(project(":domain_main_screen"))
    implementation(project(":feature_onboarding_api"))
    implementation(project(":feature_schedule_api"))

    implementation(project(":library_adapter"))
    implementation(project(":library_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_elm"))
    implementation(project(":library_navigation"))
    implementation(project(":library_network"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}
