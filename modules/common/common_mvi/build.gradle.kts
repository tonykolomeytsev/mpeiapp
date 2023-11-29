plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleExtensions)
    implementation(libs.google.material)

    implementation(libs.rx.java)
    implementation(libs.timber)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)

    implementation(project(":common_android"))
}