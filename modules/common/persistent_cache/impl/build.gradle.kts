plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    implementation(project(":common_persistent_cache_api"))
    implementation(project(":common_coroutines_api"))

    compileOnly(androidJar)
    compileOnlyAar(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotlinx.coroutines.test)
}
