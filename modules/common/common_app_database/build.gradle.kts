plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.androidx.annotation)
    compileOnlyAar(libs.koin.android)

    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.koin.core)

    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
}
