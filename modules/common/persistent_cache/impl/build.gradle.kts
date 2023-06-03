plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(project(":common_persistent_cache_api"))
    implementation(project(":common_coroutines_api"))

    compileOnly(androidJar)
    compileOnlyAar(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}
