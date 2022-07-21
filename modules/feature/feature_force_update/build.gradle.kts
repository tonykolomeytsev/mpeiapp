plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.google.material)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.firebase.config)

    implementation(project(":domain_force_update"))

    implementation(project(":coreui"))

    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_android"))
    implementation(project(":common_navigation"))
    implementation(project(":common_analytics"))
}
