plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.rx.java)

    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
}
