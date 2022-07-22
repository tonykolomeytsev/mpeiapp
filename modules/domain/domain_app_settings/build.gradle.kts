plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.google.gson)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)

    implementation(project(":common_annotations"))
    implementation(project(":common_cache"))
    implementation(project(":common_di"))
    implementation(project(":common_network"))
    implementation(project(":common_shared_preferences"))
}
