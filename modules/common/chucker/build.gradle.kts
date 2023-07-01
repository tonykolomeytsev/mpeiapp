plugins {
    id("mpeix.android.lib")
}

dependencies {
    implementation(project(":common_di"))
    implementation(libs.koin.core)
    implementation(libs.squareup.okhttp)
    implementation(libs.chucker.library)
}
