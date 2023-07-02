plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)

    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":ext_kotlin"))

    implementation(project(":domain_notes"))
    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))
}
