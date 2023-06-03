package mpeix.plugins.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
internal class KotlinConventionPlugin : Plugin<Project> {

    private val jvmTarget = JavaVersion.VERSION_11

    override fun apply(target: Project) {
        return with(target) {
            with(plugins) {
                apply("kotlin")
                apply("mpeix.aar2jar")
            }
            tasks.withType<Test> {
                useJUnitPlatform()
            }
            tasks.withType<JavaCompile> {
                sourceCompatibility = jvmTarget.toString()
                targetCompatibility = jvmTarget.toString()
            }
            tasks.withType<KotlinCompile> {
                kotlinOptions.jvmTarget = jvmTarget.toString()
            }
        }
    }
}
