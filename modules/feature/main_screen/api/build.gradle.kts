plugins {
    id("mpeix.feature.api")
}

dependencies {
    compileOnly(androidJar)
    implementation(project(":lib_navigation_api"))
}
