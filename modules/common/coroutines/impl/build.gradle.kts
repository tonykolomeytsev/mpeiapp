plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(project(":common_coroutines_api"))

    compileOnlyAar(libs.kotlinx.coroutines.android)
    compileOnlyAar(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
}
