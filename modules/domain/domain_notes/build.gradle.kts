plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnly(androidJar)

    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":common_app_database"))
    implementation(project(":common_kotlin"))
}
