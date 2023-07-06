package mpeix.plugins.setup

import com.android.build.api.dsl.CommonExtension
import mpeix.plugins.ext.debugImplementation
import mpeix.plugins.ext.implementation
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

internal fun Project.configureAndroidCompose(
    extension: CommonExtension<*, *, *, *>,
) {
    val libs = versionCatalog

    extension.composeOptions.kotlinCompilerExtensionVersion =
        libs.requiredVersion("composeCompiler")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            val composeMetricsDir = buildDir.resolve("compose-metrics")
            val composeReportsDir = buildDir.resolve("compose-reports")
            freeCompilerArgs = freeCompilerArgs +
                    getOptInExperimentalApiCompilerArgs() +
                    getComposeCompilerMetricsCompilerArgs(composeMetricsDir) +
                    getComposeCompilerReportsCompilerArgs(composeReportsDir)
        }
    }

    dependencies {
        implementation(platform(libs.findLibrary("compose.bom").get()))
        implementation(libs.findBundle("compose").get())
        implementation(libs.findBundle("accompanist").get())
        debugImplementation(libs.findLibrary("compose.ui.tooling").get())
    }
}

private fun getOptInExperimentalApiCompilerArgs(): List<String> =
    listOf(
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
    )

private fun getComposeCompilerMetricsCompilerArgs(outputDir: File): List<String> =
    listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$outputDir",
    )

private fun getComposeCompilerReportsCompilerArgs(outputDir: File): List<String> =
    listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$outputDir"
    )
