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
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.facebook.shimmer)
    implementation(libs.google.gson)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.rmr.mapmemory.core)
    implementation(libs.rmr.mapmemory.coroutines)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)
    implementation(libs.androidx.datastore.preferences.core)

    implementation(project(":feature_app_settings_api"))
    implementation(project(":feature_bars_api"))
    implementation(project(":feature_schedule_api"))

    implementation(project(":library_adapter"))
    implementation(project(":library_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":ext_shared_preferences"))
    implementation(project(":library_elm"))
    implementation(project(":library_navigation"))
    implementation(project(":library_schedule"))
    implementation(project(":library_network"))

    implementation(project(":coreui"))
    implementation(project(":icons"))
    implementation(project(":res_strings"))
}
