plugins {
    id("mpeix.android.lib")
}

@Suppress("UnstableApiUsage")
android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.firebase.config)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":domain_force_update"))

    implementation(project(":common_analytics"))
    implementation(project(":ext_android"))
    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_navigation"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}
