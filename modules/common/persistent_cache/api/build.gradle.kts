plugins {
    id("mpeix.kotlin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)
}
