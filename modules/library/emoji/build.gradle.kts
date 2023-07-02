plugins {
    id("mpeix.android.library")
}

@Suppress("UnstableApiUsage")
android.buildFeatures.androidResources = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":library_app_info"))
}
