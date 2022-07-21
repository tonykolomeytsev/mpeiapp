plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleExtensions)
    implementation(libs.google.material)

    implementation(libs.timber)
    implementation(libs.rx.java)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.android)

    implementation(project(":common_android"))
}
