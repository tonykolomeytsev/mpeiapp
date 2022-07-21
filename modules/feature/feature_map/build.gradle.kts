plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.google.material)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.coordinatorLayout)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.rx.java)
    implementation(libs.rx.android)

    implementation(libs.squareup.retrofit)
    implementation(libs.google.maps)
    implementation(libs.google.gson)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.storeHolder)

    implementation(project(":domain_map"))
    implementation(project(":domain_app_settings"))

    implementation(project(":common_di"))
    implementation(project(":common_network"))
    implementation(project(":common_mvi"))
    implementation(project(":common_navigation"))
    implementation(project(":common_android"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_adapter"))
    implementation(project(":common_cache"))
    implementation(project(":common_analytics"))
    implementation(project(":common_emoji"))
    implementation(project(":coreui"))
}
