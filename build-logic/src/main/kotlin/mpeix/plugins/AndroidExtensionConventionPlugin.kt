package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureKotlinAndroid
import mpeix.plugins.setup.configureNamespace
import mpeix.plugins.setup.disallowProjectDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
internal class AndroidExtensionConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidLibrary)
                apply(Plugins.KotlinAndroid)
                apply(Plugins.GradleAndroidCacheFix)
                apply(Plugins.MpeixDetekt)
            }

            extensions.configure<LibraryExtension> {
                configureNamespace(this)
                configureKotlinAndroid(this)

                @Suppress("UnstableApiUsage")
                with(buildFeatures) {
                    compose = false
                    viewBinding = false
                    androidResources = false
                    shaders = false
                    resValues = false
                    buildConfig = false
                }
            }

            disallowProjectDependencies {
                "Modules of type Android Extension can only depend on external libraries"
            }
        }
    }
}
