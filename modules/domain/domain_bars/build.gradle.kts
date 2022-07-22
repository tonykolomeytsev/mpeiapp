plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.androidx.appCompat)
    implementation(libs.google.gson)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.rxJava3)
    implementation(libs.timber)

    implementation(project(":common_annotations"))
    implementation(project(":common_cache"))
    implementation(project(":common_di"))
    implementation(project(":common_shared_preferences"))
}
