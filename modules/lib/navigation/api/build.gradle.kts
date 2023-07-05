plugins {
    id("mpeix.kotlin.library")
    id("mpeix.kotlin.aar2jar")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.appyx.core)
}
