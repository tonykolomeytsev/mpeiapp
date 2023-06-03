plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.lifecycleCommonJava8)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.android)
    implementation(libs.rx.java)

    implementation(project(":common_di"))
}
