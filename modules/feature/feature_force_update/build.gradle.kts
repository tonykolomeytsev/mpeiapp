plugins {
    id("mpeix.android")
}

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
    implementation(project(":common_android"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_navigation"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}