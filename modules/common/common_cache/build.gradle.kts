plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(libs.google.gson)
    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
}
