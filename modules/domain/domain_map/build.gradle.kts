plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.rxJava3)
    implementation(libs.google.gson)
    implementation(libs.androidx.appCompat)
    implementation(libs.timber)

    implementation(project(":common_annotations"))
    implementation(project(":common_cache"))
}