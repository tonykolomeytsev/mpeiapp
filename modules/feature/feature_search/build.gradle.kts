plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)

    implementation(libs.google.material)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.rx.java)
    implementation(libs.rx.android)
    implementation(libs.facebook.shimmer)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.android)

    implementation(project(":domain_schedule"))
    implementation(project(":domain_notes"))
    implementation(project(":domain_map"))
    implementation(project(":domain_search"))

    implementation(project(":common_schedule"))
    implementation(project(":common_adapter"))
    implementation(project(":common_android"))
    implementation(project(":common_mvi"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_navigation"))
    implementation(project(":common_analytics"))
    implementation(project(":coreui"))
}