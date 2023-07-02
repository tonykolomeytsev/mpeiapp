plugins {
    id("mpeix.kotlin.lib")
    id("mpeix.ksp")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.androidx.room.common)
    compileOnlyAar(libs.androidx.sqlite)
    implementation(libs.koin.core)

    ksp(libs.androidx.room.compiler)

    implementation(project(":library_app_database_api"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":ext_koin"))
}
