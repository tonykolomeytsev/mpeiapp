package mpeix.plugins

import mpeix.plugins.features.androidjar.enableCompileOnlyAndroidJar
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import mpeix.plugins.features.globalconfig.registerAndroidProjectConfigurationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("Unused")
internal abstract class RootProjectConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        registerAndroidProjectConfigurationExtension()
        afterEvaluate {
            val config = getAndroidProjectConfiguration()
            enableCompileOnlyAndroidJar(config.targetSdk)
        }
    }
}