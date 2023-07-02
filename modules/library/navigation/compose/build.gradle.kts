plugins {
    id("mpeix.android.library")
    id("mpeix.android.compose")
}

dependencies {
    api(project(":library_navigation_api"))

    implementation(libs.appyx.core)
}
