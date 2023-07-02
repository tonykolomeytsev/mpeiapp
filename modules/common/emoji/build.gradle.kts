plugins {
    id("mpeix.android.lib")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":library_app_info"))
}
