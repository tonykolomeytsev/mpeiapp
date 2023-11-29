plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.google.gson)
    implementation(libs.google.maps)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.storeHolder)

    implementation(project(":domain_app_settings"))
    implementation(project(":domain_map"))

    implementation(project(":common_adapter"))
    implementation(project(":common_analytics"))
    implementation(project(":common_android"))
    implementation(project(":common_cache"))
    implementation(project(":common_di"))
    implementation(project(":common_emoji"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_mvi"))
    implementation(project(":common_navigation"))
    implementation(project(":common_network"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}