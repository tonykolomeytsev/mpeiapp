plugins {
    id("mpeix.feature.api")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)
    implementation(libs.google.gson)
    implementation(project(":lib_navigation_api"))
}
