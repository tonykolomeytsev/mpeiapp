plugins {
    id("mpeix.kotlin.library")
}

dependencies {
    implementation(libs.koin.core)

    implementation(project(":library_app_info"))
}
