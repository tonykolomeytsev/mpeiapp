plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.viewPager2)
    implementation(libs.firebase.analytics)
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(project(":common_di"))
    implementation(project(":domain_schedule"))
}
