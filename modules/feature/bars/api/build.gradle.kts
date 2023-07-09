plugins {
    id("mpeix.feature.api")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)
    implementation(project(":lib_navigation_api"))
}
