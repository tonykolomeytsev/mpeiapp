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
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.viewPager2)
    implementation(libs.facebook.shimmer)
    implementation(libs.google.gson)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.timber)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.storeHolder)

    implementation(project(":domain_app_settings"))
    implementation(project(":domain_main_screen"))
    implementation(project(":domain_notes"))
    implementation(project(":domain_onboarding"))
    implementation(project(":domain_schedule"))

    implementation(project(":common_adapter"))
    implementation(project(":common_analytics"))
    implementation(project(":common_android"))
    implementation(project(":common_app_database"))
    implementation(project(":common_cache"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_elm"))
    implementation(project(":common_navigation"))
    implementation(project(":common_network"))
    implementation(project(":common_schedule"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}