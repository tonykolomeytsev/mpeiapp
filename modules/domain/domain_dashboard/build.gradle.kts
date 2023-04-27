plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))
}
