plugins {
    id("mpeix.android")
}

dependencies {
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.androidx.appCompat)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(project(":common_android"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
}