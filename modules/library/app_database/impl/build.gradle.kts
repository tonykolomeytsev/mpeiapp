plugins {
    id("mpeix.android.library")
    id("mpeix.ksp")
}

dependencies {

    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    ksp(libs.androidx.room.compiler)

    implementation(project(":library_app_info"))

    implementation(project(":domain_favorite_schedule"))
    implementation(project(":domain_notes"))
    implementation(project(":domain_schedule_models"))

    implementation(project(":ext_kotlin"))

    implementation(project(":library_app_database_api"))
}
