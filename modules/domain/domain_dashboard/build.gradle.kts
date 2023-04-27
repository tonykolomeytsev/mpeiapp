plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)

    implementation(libs.koin.core)
    implementation(libs.rx.java)

    implementation(project(":common_kotlin"))

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))
}
