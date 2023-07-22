package mpeix.plugins

import mpeix.plugins.ext.implementation
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.JetBrainsSubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

/**
 * # Kotlin JVM Parcelize Plugin
 *
 * The plugin allows to use `@Parcelize` annotation in Kotlin JVM modules.
 *
 * ### Usage
 *
 * ```kotlin
 * plugins {
 *     id("mpeix.kotlin.parcelize")
 * }
 * ```
 *
 * Implementation taken from official Kotlin repository:
 * https://github.com/JetBrains/kotlin/blob/master/libraries/tools/kotlin-gradle-plugin/src/common/kotlin/org/jetbrains/kotlin/gradle/targets/android/internal/ParcelizeSubplugin.kt
 */
@Suppress("unused")
class KotlinJvmParcelizePlugin : KotlinCompilerPluginSupportPlugin {

    @Suppress("MaxLineLength")
    override fun apply(target: Project) {
        with(target) {
            val kotlinPluginVersion = getKotlinPluginVersion()
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:$kotlinPluginVersion")
            }
        }
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation is KotlinJvmCompilation

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>,
    ): Provider<List<SubpluginOption>> {
        return kotlinCompilation.target.project.provider { emptyList() }
    }

    override fun getCompilerPluginId() = "org.jetbrains.kotlin.parcelize"

    override fun getPluginArtifact(): SubpluginArtifact =
        JetBrainsSubpluginArtifact(artifactId = "kotlin-parcelize-compiler")
}
