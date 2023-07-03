plugins {
    id("mpeix.android.library")
    id("mpeix.ksp")
}

dependencies {

    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.koin.core)

    ksp(libs.androidx.room.compiler)

    implementation(project(":ext_kotlin"))
}
