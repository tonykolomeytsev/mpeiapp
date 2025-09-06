package mpeix.plugins

import mpeix.plugins.features.aar2jar.registerAarToJarTransform
import mpeix.plugins.features.androidjar.createAndroidJarAccessorExtension
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import mpeix.plugins.features.jvm.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import mpeix.plugins.setup.Plugins

internal abstract class UtilKotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply(Plugins.KotlinJvm)
            apply(Plugins.AndroidLint)
        }
        val config = getAndroidProjectConfiguration()
        configureKotlinJvm(explicitApi = true)
        createAndroidJarAccessorExtension()
        registerAarToJarTransform()
        config.defaultDependencies.orNull?.execute(dependencies)
    }
}