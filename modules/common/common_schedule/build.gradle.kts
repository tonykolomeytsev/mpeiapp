plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)

    implementation(libs.androidx.coreKtx)
    implementation(libs.google.material)

    implementation(project(":common_android"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_adapter"))
    implementation(project(":domain_schedule"))
    implementation(project(":coreui"))
}