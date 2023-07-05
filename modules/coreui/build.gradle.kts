plugins {
    id("mpeix.android.library")
}

@Suppress("UnstableApiUsage")
android.buildFeatures {
    viewBinding = true
    androidResources = true
}

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
    implementation(project(":feature_schedule_api"))
    implementation(project(":feature_favorite_schedule_api"))

    implementation(project(":ext_okhttp"))

    implementation(project(":library_elm"))
    implementation(project(":library_network"))

    implementation(project(":icons"))
    implementation(project(":res_images"))
    implementation(project(":res_strings"))
}
