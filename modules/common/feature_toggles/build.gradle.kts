plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    implementation(libs.koin.core)

    implementation(project(":common_di"))
}
