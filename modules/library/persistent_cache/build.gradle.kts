plugins {
    id("mpeix.kotlin.library")
}

dependencies {
    implementation(project(":library_coroutines"))

    compileOnly(androidJar)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotlinx.coroutines.test)
}
