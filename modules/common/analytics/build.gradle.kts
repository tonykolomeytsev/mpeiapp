plugins {
    id("mpeix.android.lib")
}

dependencies {
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.viewPager2)
    implementation(libs.firebase.analytics)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":common_di"))

    implementation(project(":domain_analytics"))
}
