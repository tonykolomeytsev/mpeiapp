plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logger)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.squareup.retrofit.rxJava3)
    implementation(libs.timber)

    implementation(project(":common_annotations"))
    implementation(project(":common_di"))
}