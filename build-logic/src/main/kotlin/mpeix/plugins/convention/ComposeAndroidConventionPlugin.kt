package mpeix.plugins.convention

import com.android.build.gradle.BaseExtension
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
            val libs = versionCatalog
            extensions.configure<BaseExtension> {
                namespace = "kekmech.ru.${target.name}"
                with(buildFeatures) {
                    compose = true
                }
                @Suppress("UnstableApiUsage")
                with(composeOptions) {
                    kotlinCompilerExtensionVersion = libs.requiredVersion("composeCompiler")
                }
            }

            dependencies {
                implementation(platform(libs.findLibrary("compose.bom").get()))
                implementation(libs.findBundle("compose").get())
                implementation(libs.findBundle("accompanist").get())
            }
        }
    }
}
