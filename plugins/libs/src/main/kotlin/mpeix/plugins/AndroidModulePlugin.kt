package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.ext.requiredVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class AndroidModulePlugin : Plugin<Project> {

    private val jvmTarget = JavaVersion.VERSION_11

    override fun apply(target: Project) {
        target.plugins.apply {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-parcelize")
            apply("mpeix.android.base")
        }
        target.extensions.configure(LibraryExtension::class.java, ::configure)
        target.dependencies.setupDependencies()
    }

    private fun DependencyHandler.setupDependencies() {
        add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:1.1.5")
    }

    private fun configure(extension: LibraryExtension) =
        with(extension) {
            compileOptions { options -> options.isCoreLibraryDesugaringEnabled = true }
        }
}