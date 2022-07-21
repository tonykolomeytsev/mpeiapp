plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.lifecycleCommonJava8)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.swipeRefreshLayout)

    implementation (libs.google.material)

    implementation (libs.koin.core)
    implementation (libs.koin.android)

    implementation (libs.rx.java)
    implementation (libs.rx.android)

    implementation (libs.timber)

    implementation(project(":common_di"))
}
