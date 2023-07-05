plugins {
    id("mpeix.feature.impl")
}

@Suppress("UnstableApiUsage")
android.buildFeatures{
    viewBinding = true
    androidResources = true
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.firebase.config)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":feature_app_update_api"))
    implementation(project(":library_app_info"))

    implementation(project(":library_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_navigation"))

    implementation(project(":coreui"))
    implementation(project(":res_strings"))
}
