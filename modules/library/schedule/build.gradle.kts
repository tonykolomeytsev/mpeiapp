plugins {
    id("mpeix.android.library")
}

@Suppress("UnstableApiUsage")
android.buildFeatures{
    viewBinding = true
    androidResources = true
}

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.google.material)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(project(":library_adapter"))
    implementation(project(":ext_android"))
    implementation(project(":ext_kotlin"))

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":coreui"))
    implementation(project(":strings"))
}
