package mpeix.plugins

import com.android.build.api.dsl.ApplicationExtension
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureAndroidCompose
import mpeix.plugins.setup.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply(Plugins.AndroidApplication)
                apply(Plugins.KotlinAndroid)
                apply(Plugins.KotlinParcelize)
                apply(Plugins.MpeixDetekt)
                apply(Plugins.GradleAndroidCacheFix)
            }

            val libs = versionCatalog

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                defaultConfig {
                    versionName = libs.requiredVersion("appVersionName")
                    versionCode = libs.requiredVersion("appVersionCode").toInt()
                }

                packaging {
                    resources {
                        excludes += setOf(
                            "**/META-INF/LICENSE*",
                            "**/META-INF/*.kotlin_module",
                        )
                    }
                }

                buildFeatures {
                    compose = true
                    viewBinding = true // TODO: remove viewBinding after complete compose migration
                    shaders = false
                    resValues = true
                    buildConfig = true
                }
            }
        }
    }
}
