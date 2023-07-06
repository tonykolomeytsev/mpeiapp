plugins {
    id("mpeix.feature.api")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)
    implementation(libs.google.gson)
}
