package mpeix.plugins.ext

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.project(path: String): Dependency =
    project(mapOf("path" to path))

fun DependencyHandler.implementation(notation: Any): Dependency? =
    add("implementation", notation)

fun DependencyHandler.coreLibraryDesugaring(notation: Any): Dependency? =
    add("coreLibraryDesugaring", notation)
