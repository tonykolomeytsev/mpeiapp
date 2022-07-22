plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.firebase.crashlytics)
    implementation(libs.google.gson)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.rxJava3)

    implementation(project(":common_android"))
    implementation(project(":common_annotations"))
    implementation(project(":common_app_database"))
    implementation(project(":common_cache"))
    implementation(project(":common_shared_preferences"))
}
