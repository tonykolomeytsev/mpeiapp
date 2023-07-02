plugins {
    id("mpeix.android.lib")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.koin.core)

    implementation(project(":common_android"))
    implementation(project(":library_app_lifecycle"))
    implementation(project(":library_app_info"))
    implementation(project(":ext_kotlin"))
    implementation(project(":library_network"))

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))
    implementation(project(":domain_app_settings"))
    implementation(project(":domain_app_settings_models"))

    implementation(project(":ext_gson"))
    implementation(project(":ext_koin"))
}
