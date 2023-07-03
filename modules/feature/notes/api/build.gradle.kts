plugins {
    id("mpeix.feature.api")
}

dependencies {
    compileOnly(androidJar)

    implementation(project(":library_app_database_api"))
    implementation(project(":ext_kotlin"))

    implementation(project(":domain_schedule_models"))

    implementation(project(":ext_koin"))
}
