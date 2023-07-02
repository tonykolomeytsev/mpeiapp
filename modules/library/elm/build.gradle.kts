plugins {
    id("mpeix.android.library")
    id("mpeix.android.compose")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.appyx.core)
    implementation(libs.google.material)
    implementation(libs.koin.compose)

    implementation(libs.rx.java)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)

    implementation(project(":common_android"))
    implementation(project(":library_navigation_api"))

    testImplementation(kotlin("test"))
}
