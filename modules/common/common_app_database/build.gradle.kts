plugins {
    id("mpeix.android")
    id("mpeix.ksp")
}

dependencies {

    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rx3)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    ksp(libs.androidx.room.compiler)

    implementation(project(":common_app_database_api"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))

    implementation(project(":domain_favorite_schedule"))
    implementation(project(":domain_notes"))
    implementation(project(":domain_schedule_models"))
}
