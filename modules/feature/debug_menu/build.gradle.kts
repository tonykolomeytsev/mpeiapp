plugins {
    id("mpeix.android.lib")
    id("mpeix.android.compose")
}

dependencies {
    implementation(project(":ui_theme"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.koin.core)
    implementation(libs.compose.activity)
}
