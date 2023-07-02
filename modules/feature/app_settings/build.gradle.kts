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

    implementation(project(":domain_app_settings"))
    implementation(project(":domain_app_settings_models"))
    implementation(project(":domain_favorite_schedule"))
    implementation(project(":domain_github"))
    implementation(project(":domain_onboarding"))
    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":common_adapter"))
    implementation(project(":common_analytics"))
    implementation(project(":common_android"))
    implementation(project(":common_di"))
    implementation(project(":common_feature_toggles"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_elm"))
    implementation(project(":common_navigation"))
    implementation(project(":library_network"))

    implementation(project(":ext_koin"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}
