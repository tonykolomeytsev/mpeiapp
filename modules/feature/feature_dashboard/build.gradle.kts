plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.timber)
    implementation(libs.rx.java)
    implementation(libs.rx.android)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.google.material)
    implementation(libs.facebook.shimmer)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.storeHolder)

    implementation(project(":domain_schedule"))
    implementation(project(":domain_notes"))
    implementation(project(":domain_search"))
    implementation(project(":domain_app_settings"))

    implementation(project(":common_schedule"))
    implementation(project(":common_di"))
    implementation(project(":common_annotations"))
    implementation(project(":common_shared_preferences"))
    implementation(project(":common_android"))
    implementation(project(":common_adapter"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_mvi"))
    implementation(project(":common_navigation"))
    implementation(project(":common_analytics"))
    implementation(project(":coreui"))
}