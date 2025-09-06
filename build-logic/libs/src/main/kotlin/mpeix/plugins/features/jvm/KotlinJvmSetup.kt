package mpeix.plugins.features.jvm

import mpeix.plugins.ext.configure
import mpeix.plugins.features.unittesting.setupUnitTests
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal fun Project.configureKotlinJvm() {
    setupUnitTests()
    extensions.configure<KotlinJvmProjectExtension> {
//        explicitApi()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    extensions.configure<JavaPluginExtension> {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
}
