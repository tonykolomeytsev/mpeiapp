plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(libs.google.gson)
    implementation(libs.koin.core)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)

    implementation(project(":common_annotations"))
    implementation(project(":common_cache"))
    implementation(project(":common_di"))
    implementation(project(":common_network"))
}