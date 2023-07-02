plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)

    implementation(libs.koin.core)
    implementation(libs.squareup.retrofit)

    implementation(project(":common_annotations"))
    implementation(project(":common_network"))

    implementation(project(":library_persistent_cache"))
}
