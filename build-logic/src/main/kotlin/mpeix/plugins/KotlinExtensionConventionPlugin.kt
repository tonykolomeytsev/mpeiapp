package mpeix.plugins

import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureKotlinJvm
import mpeix.plugins.setup.disallowProjectDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KotlinExtensionConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.KotlinJvm)
                apply(Plugins.MpeixDetekt)
            }
            configureKotlinJvm()

            disallowProjectDependencies {
                "Modules of type Kotlin Extension can only depend on external libraries"
            }
        }
    }
}
