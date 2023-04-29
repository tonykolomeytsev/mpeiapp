plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.androidx.sqlite)
}
