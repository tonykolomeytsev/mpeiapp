plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(libs.rx.java)
    implementation(libs.koin.core)
    implementation(libs.google.gson)

    implementation(project(":common_kotlin"))
    implementation(project(":common_di"))
}
