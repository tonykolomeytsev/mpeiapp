plugins {
    id("mpeix.android")
}

android.buildFeatures.viewBinding = true

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.google.material)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(project(":common_adapter"))
    implementation(project(":common_android"))
    implementation(project(":common_kotlin"))

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}
