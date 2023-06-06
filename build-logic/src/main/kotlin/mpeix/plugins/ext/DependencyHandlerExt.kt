package mpeix.plugins.ext

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

internal fun DependencyHandler.project(path: String): Dependency =
    project(mapOf("path" to path))

internal fun DependencyHandler.implementation(notation: Any): Dependency? =
    add("implementation", notation)

internal fun DependencyHandler.debugImplementation(notation: Any): Dependency? =
    add("debugImplementation", notation)

internal fun DependencyHandler.coreLibraryDesugaring(notation: Any): Dependency? =
    add("coreLibraryDesugaring", notation)
