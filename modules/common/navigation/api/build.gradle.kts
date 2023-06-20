plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.appyx.core)
}
