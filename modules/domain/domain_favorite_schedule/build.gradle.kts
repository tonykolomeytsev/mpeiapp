plugins {
    id("mpeix.android")
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":common_kotlin"))
    implementation(project(":common_app_database"))
    implementation(project(":common_di"))

    implementation(project(":domain_schedule_models"))
}
