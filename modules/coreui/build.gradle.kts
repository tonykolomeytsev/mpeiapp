plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.coreKtx)
    implementation(libs.google.material)
    implementation(libs.facebook.shimmer)
    implementation(libs.rx.java)
    implementation(libs.rx.android)
    implementation(libs.squareup.retrofit)

    implementation(project(":domain_schedule"))
    implementation(project(":domain_notes"))

    implementation(project(":common_android"))
    implementation(project(":common_adapter"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_network"))
    implementation(project(":common_cache"))
}