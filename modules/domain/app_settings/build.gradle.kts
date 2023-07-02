plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnly(androidJar)

    implementation(libs.rx.java)

    implementation(project(":common_feature_toggles"))
    implementation(project(":ext_shared_preferences"))

    implementation(project(":domain_app_settings_models"))
}
