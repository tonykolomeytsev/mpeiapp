plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.facebook.shimmer)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.java)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)

    implementation(project(":domain_notes"))
    implementation(project(":domain_schedule"))
    implementation(project(":domain_search"))

    implementation(project(":common_adapter"))
    implementation(project(":common_analytics"))
    implementation(project(":common_android"))
    implementation(project(":common_app_database"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_mvi"))
    implementation(project(":common_navigation"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}