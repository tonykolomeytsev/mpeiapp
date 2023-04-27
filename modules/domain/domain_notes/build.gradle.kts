plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":common_android"))
    implementation(project(":common_app_database"))
}
