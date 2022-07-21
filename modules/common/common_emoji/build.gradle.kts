plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.androidx.appCompat)

    implementation(project(":common_di"))
}
