plugins {
    id("mpeix.kotlin.lib")
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
    implementation(project(":common_di"))
    implementation(project(":common_network"))

    implementation(project(":ext_shared_preferences"))

    implementation(project(":library_persistent_cache"))
}
