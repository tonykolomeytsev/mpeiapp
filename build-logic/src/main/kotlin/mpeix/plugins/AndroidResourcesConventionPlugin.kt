package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class AndroidResourcesConventionPlugin : Plugin<Project> {

    @Suppress("UnstableApiUsage")
    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidLibrary)
                apply(Plugins.GradleAndroidCacheFix)
            }

            val libs = versionCatalog

            extensions.configure<LibraryExtension> {
                configureNamespace(this)

                compileSdk = libs.requiredVersion("compileSdk").toInt()
                buildToolsVersion = libs.requiredVersion("buildTools")

                defaultConfig {
                    minSdk = libs.requiredVersion("minSdk").toInt()
                }

                buildFeatures {
                    compose = false
                    viewBinding = false
                    androidResources = true
                    shaders = false
                    resValues = false
                    buildConfig = false
                }
            }
        }
    }
}
