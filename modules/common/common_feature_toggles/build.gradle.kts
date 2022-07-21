plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(libs.koin.core)

    implementation(project(":common_di"))
}