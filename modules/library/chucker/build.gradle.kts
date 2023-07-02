plugins {
    id("mpeix.android.library")
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.squareup.okhttp)
    implementation(libs.chucker.library)
    implementation(project(":ext_koin"))
}
