plugins {
    id("mpeix.kotlin.library")
}

dependencies {
    compileOnly(androidJar)

    implementation(libs.koin.core)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logger)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)

    implementation(project(":common_di"))

    implementation(project(":domain_app_settings_models"))

    implementation(project(":ext_gson"))
    implementation(project(":ext_koin"))
    implementation(project(":ext_okhttp"))
}
