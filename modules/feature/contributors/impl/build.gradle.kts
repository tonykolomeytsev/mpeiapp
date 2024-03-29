plugins {
    id("mpeix.feature.impl")
}

dependencies {
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.squareup.retrofit)

    implementation(project(":lib_network"))
    implementation(project(":lib_persistent_cache"))

    implementation(project(":feature_contributors_api"))
}
