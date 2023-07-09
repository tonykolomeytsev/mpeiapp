plugins {
    id("mpeix.feature.api")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.appyx.core)
    implementation(project(":lib_navigation_api"))
}
