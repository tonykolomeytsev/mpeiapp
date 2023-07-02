plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)

    implementation(libs.koin.core)
    implementation(libs.squareup.retrofit)

    implementation(project(":library_network"))

    implementation(project(":library_persistent_cache"))
}
