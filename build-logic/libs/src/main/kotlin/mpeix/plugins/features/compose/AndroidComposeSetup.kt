package mpeix.plugins.features.compose

import mpeix.plugins.ext.configure
import mpeix.plugins.ext.configureLazy
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import mpeix.plugins.setup.Plugins
import org.gradle.api.Project
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidCompose() {
    // Compose compiler plugin will override the coordinates of the Compose
    // compiler supplied automatically by AGP
    plugins.apply(Plugins.ComposeCompiler)
    val config = getAndroidProjectConfiguration()
    tasks.configureLazy<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll(getOptInCompilerArgs())
        }
    }
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        val enableComposeMetrics = providers.gradleProperty("enableComposeMetrics")
            .map { it.toBoolean() }
            .orElse(false)
        if (enableComposeMetrics.get()) {
            val metricsDir = layout.buildDirectory.dir("compose_metrics")
            reportsDestination.set(metricsDir)
            metricsDestination.set(metricsDir)
        }
    }
    config.defaultComposeDependencies.orNull?.execute(dependencies)
}

private fun getOptInCompilerArgs(): List<String> =
    listOf(
        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
        "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
    )
