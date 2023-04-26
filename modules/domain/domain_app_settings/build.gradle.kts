plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnly(androidJar)

    implementation(libs.google.gson)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)

    implementation(project(":common_annotations"))
    implementation(project(":common_cache"))
    implementation(project(":common_di"))
    implementation(project(":common_network"))
    implementation(project(":common_shared_preferences"))

    implementation(project(":domain_app_settings_models"))
}
