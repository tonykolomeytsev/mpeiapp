plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    kotlin("android") version "1.7.0" apply false
    id("com.google.gms.google-services") version "4.3.13" apply false
    id("com.google.firebase.crashlytics") version "2.9.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.20.0"

    id("mpeix.android") apply false
    id("mpeix.kotlin") apply false
}

tasks.withType<Delete>() {
    delete(rootProject.buildDir)
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }
    detekt {
        config = rootProject.files("detekt.yaml")
    }
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")
    }
}
