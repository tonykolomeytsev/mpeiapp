plugins {
    id("mpeix.feature.impl")
}

@Suppress("UnstableApiUsage")
android.buildFeatures {
    viewBinding = true
    androidResources = true
}

dependencies {
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.viewPager2)
    implementation(libs.facebook.shimmer)
    implementation(libs.google.material)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.picasso)
    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.coroutines)

    implementation(project(":feature_app_settings_api"))
    implementation(project(":feature_favorite_schedule_api"))
    implementation(project(":feature_contributors_api"))
    implementation(project(":feature_schedule_api"))

    implementation(project(":lib_adapter"))
    implementation(project(":lib_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":lib_app_info"))
    implementation(project(":lib_feature_toggles"))
    implementation(project(":ext_kotlin"))
    implementation(project(":lib_elm"))
    implementation(project(":lib_navigation"))
    implementation(project(":lib_network"))

    implementation(project(":ext_koin"))
    implementation(project(":ext_shared_preferences"))

    implementation(project(":coreui"))
    implementation(project(":res_strings"))
}
