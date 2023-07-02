plugins {
    id("mpeix.android.lib")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":common_di"))
    implementation(project(":ext_kotlin"))
}
