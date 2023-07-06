package mpeix.plugins.setup

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinJvm() {
    val javaVersion = JavaVersion.VERSION_11.toString()

    tasks.withType<Test> {
        useJUnitPlatform()
    }
    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion
    }
}
