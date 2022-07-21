plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.google.gson)

    implementation(project(":common_di"))
    implementation(project(":common_network"))
    implementation(project(":common_annotations"))
    implementation(project(":common_shared_preferences"))
    implementation(project(":common_cache"))
}
