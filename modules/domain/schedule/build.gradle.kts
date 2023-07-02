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

    implementation(project(":library_analytics_api"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":ext_kotlin"))
    implementation(project(":library_app_info"))
    implementation(project(":library_network"))

    implementation(project(":ext_shared_preferences"))

    implementation(project(":library_persistent_cache"))
}
