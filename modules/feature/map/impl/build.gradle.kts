plugins {
    id("mpeix.feature.impl")
}

@Suppress("UnstableApiUsage")
android.buildFeatures {
    viewBinding = true
    androidResources = true
}

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
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)
    implementation(libs.squareup.retrofit)

    implementation(project(":feature_app_settings_api"))
    implementation(project(":feature_map_api"))

    implementation(project(":library_adapter"))
    implementation(project(":library_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":library_app_lifecycle"))
    implementation(project(":library_app_info"))
    implementation(project(":library_emoji"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_elm"))
    implementation(project(":library_navigation"))
    implementation(project(":library_network"))
    implementation(project(":library_persistent_cache"))

    implementation(project(":ext_koin"))

    implementation(project(":coreui"))
    implementation(project(":res_strings"))
}
