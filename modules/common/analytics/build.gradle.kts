plugins {
    id("mpeix.android.lib")
}

dependencies {
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.viewPager2)
    implementation(libs.firebase.analytics)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":common_app_lifecycle"))
    implementation(project(":common_di"))
    implementation(project(":common_network"))

    implementation(project(":domain_analytics"))

    implementation(project(":ext_koin"))
}
