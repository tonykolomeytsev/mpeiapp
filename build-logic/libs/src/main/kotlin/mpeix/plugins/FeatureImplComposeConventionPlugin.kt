package mpeix.plugins

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.dsl.LibraryExtension
import mpeix.plugins.ext.configure
import mpeix.plugins.features.android.configureAndroidNamespace
import mpeix.plugins.features.android.configureKotlinAndroid
import mpeix.plugins.features.android.disableAndroidTestTasks
import mpeix.plugins.features.android.enableOnlyDebugBuildType
import mpeix.plugins.features.compose.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import mpeix.plugins.setup.Plugins
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal abstract class FeatureImplComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply(Plugins.AndroidLibrary)
            apply(Plugins.GradleAndroidCacheFix)
        }
        configureAndroidCompose()
        extensions.configure<LibraryExtension> {
            configureAndroidNamespace(this)
            configureKotlinAndroid(explicitApi = false)

            androidResources.enable = false
            with(buildFeatures) {
                compose = true
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
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFiles.add(
                rootProject.layout.projectDirectory
                    .file("gradle/compose-stability-config.conf")
            )
        }
    }
}