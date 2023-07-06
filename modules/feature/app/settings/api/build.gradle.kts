plugins {
    id("mpeix.feature.api")
}

dependencies {
    implementation(project(":lib_feature_toggles"))
    implementation(project(":lib_network"))
}
