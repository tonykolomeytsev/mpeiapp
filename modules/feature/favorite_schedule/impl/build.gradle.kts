plugins {
    id("mpeix.feature.impl")
}

dependencies {
    implementation(libs.androidx.sqlite)
    implementation(libs.koin.core)
    implementation(project(":domain_schedule_models"))
    implementation(project(":feature_favorite_schedule_api"))
    implementation(project(":library_app_database"))
    implementation(project(":ext_kotlin"))
    implementation(project(":ext_koin"))
}
