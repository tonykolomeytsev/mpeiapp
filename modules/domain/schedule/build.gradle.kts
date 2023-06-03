plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.androidx.fragment)
    compileOnlyAar(libs.firebase.crashlytics)

    implementation(libs.google.gson)
    implementation(libs.koin.core)
    implementation(libs.squareup.retrofit)

    implementation(project(":domain_analytics"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":common_kotlin"))
    implementation(project(":common_annotations"))
    implementation(project(":common_persistent_cache_api"))
    implementation(project(":common_di"))
    implementation(project(":common_network"))
    implementation(project(":common_shared_preferences"))
}
