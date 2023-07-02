plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    implementation(libs.google.gson)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.squareup.retrofit)

    implementation(project(":library_network"))

    implementation(project(":library_persistent_cache"))
}
