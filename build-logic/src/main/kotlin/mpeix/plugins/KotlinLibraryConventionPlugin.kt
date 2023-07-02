package mpeix.plugins

import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KotlinLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.KotlinJvm)
                apply(Plugins.MpeixDetekt)
            }
            configureKotlinJvm()
        }
    }
}
