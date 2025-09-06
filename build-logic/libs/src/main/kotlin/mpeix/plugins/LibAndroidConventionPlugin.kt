package mpeix.plugins

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import mpeix.plugins.ext.configure
import mpeix.plugins.features.android.configureAndroidNamespace
import mpeix.plugins.features.android.configureKotlinAndroid
import mpeix.plugins.features.android.disableAndroidTestTasks
import mpeix.plugins.features.android.enableOnlyDebugBuildType
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import mpeix.plugins.setup.Plugins

internal abstract class LibAndroidConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply(Plugins.AndroidLibrary)
            apply(Plugins.KotlinAndroid)
            apply(Plugins.GradleAndroidCacheFix)
        }
        val config = getAndroidProjectConfiguration()
        extensions.configure<LibraryExtension> {
            configureAndroidNamespace(this)
            configureKotlinAndroid(explicitApi = true)

            with(buildFeatures) {
                compose = false
                viewBinding = false
                androidResources = false
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
