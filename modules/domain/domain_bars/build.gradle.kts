plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.rxJava3)
    implementation(libs.google.gson)
    implementation(libs.androidx.appCompat)
    implementation(libs.rx.java)
    implementation(libs.timber)

    implementation(project(":common_di"))
    implementation(project(":common_annotations"))
    implementation(project(":common_shared_preferences"))
    implementation(project(":common_cache"))
}
