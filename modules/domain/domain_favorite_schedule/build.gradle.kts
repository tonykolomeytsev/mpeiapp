plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":common_android"))
    implementation(project(":common_app_database"))
    implementation(project(":common_di"))
}