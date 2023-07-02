plugins {
    id("mpeix.kotlin.library")
}

dependencies {
    implementation(libs.koin.core)

    implementation(project(":common_di"))
}
