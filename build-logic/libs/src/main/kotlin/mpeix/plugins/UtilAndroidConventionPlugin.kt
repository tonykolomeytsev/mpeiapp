package mpeix.plugins

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import mpeix.plugins.ext.configure
import mpeix.plugins.features.android.configureAndroidNamespace
import mpeix.plugins.features.android.configureKotlinAndroid
import mpeix.plugins.features.android.disableAndroidTestTasks
import mpeix.plugins.features.android.enableOnlyDebugBuildType
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import mpeix.plugins.setup.Plugins
import org.gradle.api.Plugin
import org.gradle.api.Project

internal abstract class UtilAndroidConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply(Plugins.AndroidLibrary)
            apply(Plugins.GradleAndroidCacheFix)
        }

        val config = getAndroidProjectConfiguration()
        extensions.configure<LibraryExtension> {
            configureAndroidNamespace()
            configureKotlinAndroid(config, explicitApi = true)

            androidResources.enable = false
            with(buildFeatures) {
                compose = false
                viewBinding = false
                shaders = false
                resValues = false
                buildConfig = false
            }
        }
        extensions.configure<LibraryAndroidComponentsExtension> {
            enableOnlyDebugBuildType()
            disableAndroidTestTasks()
        }
        config.defaultDependencies.orNull?.execute(dependencies)
        config.defaultAndroidDependencies.orNull?.execute(dependencies)
    }
}
