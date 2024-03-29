plugins {
    id("mpeix.feature.api")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    implementation(project(":lib_app_info"))
    implementation(project(":ext_kotlin"))

    implementation(project(":feature_schedule_api"))

    implementation(project(":ext_koin"))
}
