plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnly(androidJar)

    testImplementation(kotlin("test"))
}
