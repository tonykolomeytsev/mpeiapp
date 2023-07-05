plugins {
    id("mpeix.android.library")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.koin.core)

    implementation(project(":ext_android"))
    implementation(project(":library_app_lifecycle"))
    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_network"))

    implementation(project(":feature_schedule_api"))
    implementation(project(":feature_app_settings_api"))

    implementation(project(":ext_gson"))
    implementation(project(":ext_koin"))
}
