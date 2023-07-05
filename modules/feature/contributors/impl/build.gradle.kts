plugins {
    id("mpeix.feature.impl")
}

dependencies {
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.squareup.retrofit)

    implementation(project(":library_network"))
    implementation(project(":library_persistent_cache"))

    implementation(project(":feature_contributors_api"))
}
