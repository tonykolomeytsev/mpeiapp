plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.google.material)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.timber)
    implementation(libs.tinkoff.pagerIndicator)
    implementation(libs.rx.java)

    implementation(project(":domain_onboarding"))
    implementation(project(":domain_schedule"))
    implementation(project(":domain_main_screen"))

    implementation(project(":common_di"))
    implementation(project(":common_android"))
    implementation(project(":common_network"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_navigation"))
    implementation(project(":common_mvi"))
    implementation(project(":common_analytics"))
    implementation(project(":common_adapter"))
    implementation(project(":coreui"))
}