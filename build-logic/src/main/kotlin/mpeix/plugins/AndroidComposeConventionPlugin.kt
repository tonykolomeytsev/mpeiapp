package mpeix.plugins

import com.android.build.gradle.BaseExtension
import mpeix.plugins.ext.debugImplementation
import mpeix.plugins.ext.implementation
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
internal class AndroidComposeConventionPlugin : Plugin<Project> {

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

            tasks.withType<KotlinCompile>() {
                kotlinOptions {
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                        "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                    )
                }
            }

            dependencies {
                implementation(platform(libs.findLibrary("compose.bom").get()))
                implementation(libs.findBundle("compose").get())
                implementation(libs.findBundle("accompanist").get())
                debugImplementation(libs.findLibrary("compose.ui.tooling").get())
            }
        }
    }
}
