plugins {
    id("mpeix.android.library")
}

dependencies {
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.viewPager2)
    implementation(libs.firebase.analytics)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":library_app_lifecycle"))
    implementation(project(":library_app_info"))
    implementation(project(":library_network"))

    implementation(project(":library_analytics_api"))

    implementation(project(":ext_koin"))
}
