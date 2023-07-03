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

    implementation(project(":library_adapter"))
    implementation(project(":ext_android"))
    implementation(project(":ext_kotlin"))

    implementation(project(":feature_notes_api"))
    implementation(project(":domain_schedule"))
    implementation(project(":feature_favorite_schedule_api"))

    implementation(project(":ext_okhttp"))

    implementation(project(":library_elm"))
    implementation(project(":library_network"))

    implementation(project(":icons"))
    implementation(project(":images"))
    implementation(project(":strings"))
}
