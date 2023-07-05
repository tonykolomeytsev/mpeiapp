plugins {
    id("mpeix.kotlin.library")
    id("mpeix.kotlin.aar2jar")
}

dependencies {
    compileOnlyAar(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
}
