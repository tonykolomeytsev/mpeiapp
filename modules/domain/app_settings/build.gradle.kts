plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnly(androidJar)

    implementation(libs.rx.java)

    implementation(project(":common_shared_preferences"))

    implementation(project(":domain_app_settings_models"))
}
