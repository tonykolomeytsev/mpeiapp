import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.0" apply false
}

subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}