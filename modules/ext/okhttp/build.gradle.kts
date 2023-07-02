plugins {
    id("mpeix.kotlin.extension")
}

dependencies {
    compileOnly(androidJar)
    implementation(libs.squareup.okhttp)
}
