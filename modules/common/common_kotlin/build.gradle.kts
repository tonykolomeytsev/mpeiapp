plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnly(androidJar)

    testImplementation(kotlin("test"))
}
