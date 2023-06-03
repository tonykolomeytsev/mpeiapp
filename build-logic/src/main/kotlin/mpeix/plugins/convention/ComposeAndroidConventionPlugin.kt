package mpeix.plugins.convention

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.ext.implementation
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
internal class ComposeAndroidConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply("com.android.library")
                apply("mpeix.android.base")
            }

            val libs = versionCatalog
            extensions.configure<LibraryExtension> {
                namespace = "kekmech.ru.${target.name}"
                @Suppress("UnstableApiUsage")
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = libs.requiredVersion("composeCompiler")
                }
            }

            dependencies {
                implementation(libs.findBundle("compose").get())
                implementation(libs.findBundle("accompanist").get())
            }
        }
    }
}
