plugins {
    id("mpeix.kotlin.library")
}

dependencies {
    compileOnly(androidJar)

    testImplementation(kotlin("test"))
}
