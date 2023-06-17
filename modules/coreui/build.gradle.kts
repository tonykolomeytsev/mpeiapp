plugins {
    id("mpeix.android.lib")
}

@Suppress("UnstableApiUsage")
android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.facebook.shimmer)
    implementation(libs.google.material)
    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)

    implementation(project(":domain_notes"))
    implementation(project(":domain_schedule"))
    implementation(project(":domain_favorite_schedule"))

    implementation(project(":common_adapter"))
    implementation(project(":common_android"))
    implementation(project(":common_elm"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_network"))

    implementation(project(":icons"))
    implementation(project(":images"))
    implementation(project(":strings"))
}
