plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(libs.google.gson)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.squareup.retrofit)

    implementation(project(":common_annotations"))
    implementation(project(":common_persistent_cache_api"))
    implementation(project(":common_network"))
}
