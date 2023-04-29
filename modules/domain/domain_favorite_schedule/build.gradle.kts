plugins {
    id("mpeix.kotlin")
    id("mpeix.ksp")
}

dependencies {
    compileOnlyAar(libs.androidx.room.common)
    compileOnlyAar(libs.androidx.sqlite)

    implementation(libs.koin.core)
    implementation(libs.rx.java)

    ksp(libs.androidx.room.compiler)

    implementation(project(":common_app_database_api"))
    implementation(project(":common_kotlin"))

    implementation(project(":domain_schedule_models"))
}
