plugins {
    id("mpeix.feature.api")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":lib_feature_toggles"))
    implementation(project(":lib_network"))
}
