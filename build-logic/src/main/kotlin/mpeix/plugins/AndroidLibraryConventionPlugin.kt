package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
internal class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidLibrary)
                apply(Plugins.KotlinAndroid)
                apply(Plugins.GradleAndroidCacheFix)
                apply(Plugins.MpeixDetekt)
            }

            extensions.configure<LibraryExtension> {
                namespace = "kekmech.ru.$name"
                configureKotlinAndroid(this)

                @Suppress("UnstableApiUsage")
                with(buildFeatures) {
                    compose = false
                    viewBinding = false
                    androidResources = false
                    shaders = false
                    resValues = false
                }
            }
        }
    }
}
