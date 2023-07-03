plugins {
    id("mpeix.feature.api")
}

dependencies {
    implementation(project(":library_feature_toggles"))
    implementation(project(":library_network"))
}
