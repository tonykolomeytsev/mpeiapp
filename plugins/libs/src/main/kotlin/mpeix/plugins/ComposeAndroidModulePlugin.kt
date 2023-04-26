package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.ext.implementation
import mpeix.plugins.ext.requiredVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler

@Suppress("unused")
class ComposeAndroidModulePlugin : Plugin<Project> {

    @Suppress("UnstableApiUsage")
    override fun apply(target: Project) {
        target.plugins.apply {
            apply("com.android.library")
            apply("mpeix.android.base")
        }
        val catalog = target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        target.extensions.configure(LibraryExtension::class.java) { extension ->
            // configure namespace of the package (instead of AndroidManifest.xml)
            extension.namespace = "kekmech.ru.${target.name}"
            extension.buildFeatures { compose = true }
            extension.composeOptions { options ->
                options.kotlinCompilerExtensionVersion = catalog.requiredVersion("composeCompiler")
            }
        }
        target.dependencies.setupDependencies(catalog)
    }

    private fun DependencyHandler.setupDependencies(catalog: VersionCatalog) {
        catalog.findBundle("compose").ifPresent { bundleProvider ->
            implementation(bundleProvider)
        }
        catalog.findBundle("accompanist").ifPresent { bundleProvider ->
            implementation(bundleProvider)
        }
    }
}
