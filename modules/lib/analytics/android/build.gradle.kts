plugins {
    id("mpeix.android.library")
}

dependencies {
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.viewPager2)
    implementation(libs.firebase.analytics)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":lib_app_lifecycle"))
    implementation(project(":lib_app_info"))
    implementation(project(":lib_network"))

    implementation(project(":lib_analytics_api"))

    implementation(project(":ext_koin"))
}
