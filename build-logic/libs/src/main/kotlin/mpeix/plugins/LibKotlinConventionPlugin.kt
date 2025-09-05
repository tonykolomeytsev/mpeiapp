package mpeix.plugins

import mpeix.plugins.features.jvm.configureKotlinJvm
import mpeix.plugins.features.aar2jar.registerAarToJarTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import mpeix.plugins.setup.Plugins
import mpeix.plugins.features.androidjar.createAndroidJarAccessorExtension
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration

internal abstract class LibKotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply(Plugins.KotlinJvm)
            apply(Plugins.AndroidLint)
        }
        val config = getAndroidProjectConfiguration()
        configureKotlinJvm()
        createAndroidJarAccessorExtension()
        registerAarToJarTransform()
        config.defaultDependencies.orNull?.execute(dependencies)
    }
}