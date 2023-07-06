package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureAndroidCompose
import mpeix.plugins.setup.configureKotlinAndroid
import mpeix.plugins.setup.configureNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class FeatureImplementationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidLibrary)
                apply(Plugins.KotlinAndroid)
                apply(Plugins.KotlinParcelize)
                apply(Plugins.GradleAndroidCacheFix)
                apply(Plugins.MpeixDetekt)
            }

            extensions.configure<LibraryExtension> {
                configureNamespace(this)
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                @Suppress("UnstableApiUsage")
                with(buildFeatures) {
                    compose = true
                    viewBinding = false
                    androidResources = false
                    shaders = false
                    resValues = false
                    buildConfig = false
                }
            }
        }
    }
}
