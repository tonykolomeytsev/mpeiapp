plugins {
    id("mpeix.android.library")
}

@Suppress("UnstableApiUsage")
android.buildFeatures.androidResources = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
}
