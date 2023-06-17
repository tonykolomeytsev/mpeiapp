package mpeix.plugins

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

/**
 * # Detekt Plugin
 *
 * A simple plugin that configures detekt in each subproject in which it is applied
 */
@Suppress("unused")
class DetektConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val libs = versionCatalog

            with(plugins) {
                val detektPluginProvider = libs.findPlugin("detekt").get()
                apply(detektPluginProvider.get().pluginId)
            }

            detekt {
                config = rootProject.files("detekt.yaml")
                parallel = true
            }

            tasks.withType<Detekt> {
                with(reports) {
                    xml.required.set(false)
                    html.required.set(true)
                    txt.required.set(false)
                    sarif.required.set(false)
                    md.required.set(false)
                }
            }

            dependencies {
                val version = libs.requiredVersion("detekt")
                detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$version")
            }
        }
    }

    private fun DependencyHandler.detektPlugins(notation: Any): Dependency? =
        add("detektPlugins", notation)

    private fun Project.detekt(closure: DetektExtension.() -> Unit) {
        closure.invoke(extensions.getByType())
    }
}
