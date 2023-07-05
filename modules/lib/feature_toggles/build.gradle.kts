plugins {
    id("mpeix.kotlin.library")
}

dependencies {
    implementation(libs.koin.core)

    implementation(project(":lib_app_info"))
}
