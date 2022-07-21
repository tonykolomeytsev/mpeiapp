package mpeix.plugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class KotlinModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply {
            apply("kotlin")
        }
        target.tasks.withType(Test::class.java) { task -> task.useJUnitPlatform() }
        target.configureJvmTarget(JavaVersion.VERSION_11)
    }

    private fun Project.configureJvmTarget(jvmTarget: JavaVersion) {
        tasks.withType(JavaCompile::class.java) { task ->
            task.sourceCompatibility = jvmTarget.toString()
            task.targetCompatibility = jvmTarget.toString()
        }
        tasks.withType(KotlinCompile::class.java) { task ->
            task.kotlinOptions.jvmTarget = jvmTarget.toString()
        }
    }
}

