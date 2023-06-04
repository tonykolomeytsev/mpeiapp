package mpeix.plugins.support

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import java.io.File

/**
 * # KSP Support Plugin
 *
 * The plugin includes support for Kotlin Symbol Processing in the project it is
 * applied to. Supports both android and pure-kotlin subprojects.
 *
 * ### Usage
 *
 * Apply plugin in module/subproject:
 *
 * ```kotlin
 * plugins {
 *     id("mpeix.ksp")
 * }
 * ```
 */
@Suppress("unused")
class KspSupportPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply("com.google.devtools.ksp")
            }

            extensions.getByType<KspExtension>().apply {
                configureRoom(target.projectDir)
            }

            extensions.findByType<KotlinProjectExtension>()?.apply {
                sourceSets.named("main").configure { sourceSet ->
                    sourceSet.kotlin.srcDir("build/generated/ksp/main/kotlin")
                }
                sourceSets.named("test").configure { sourceSet ->
                    sourceSet.kotlin.srcDir("build/generated/ksp/test/kotlin")
                }
            }
        }
    }

    private fun KspExtension.configureRoom(projectDir: File) {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
    }
}
