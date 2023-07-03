plugins {
    id("mpeix.android.lib")
}

@Suppress("UnstableApiUsage")
android.buildFeatures.viewBinding = true

dependencies {
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)
    
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.facebook.shimmer)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)

    implementation(project(":feature_app_settings_api"))
    implementation(project(":domain_dashboard"))
    implementation(project(":domain_favorite_schedule"))
    implementation(project(":domain_notes"))
    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))
    implementation(project(":domain_search"))

    implementation(project(":library_adapter"))
    implementation(project(":library_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_elm"))
    implementation(project(":library_navigation"))
    implementation(project(":library_schedule"))
    implementation(project(":ext_shared_preferences"))

    implementation(project(":coreui"))
    implementation(project(":icons"))
    implementation(project(":strings"))
}
