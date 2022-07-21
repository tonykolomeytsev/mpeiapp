plugins {
    id("mpeix.android")
}

dependencies {
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)

    implementation(libs.androidx.appCompat)
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_android"))
}