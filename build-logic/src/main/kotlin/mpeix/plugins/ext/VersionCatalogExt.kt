package mpeix.plugins.ext

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Get project's Version Catalog named 'libs' or throw
 */
internal val Project.versionCatalog: VersionCatalog
    get() = extensions
        .getByType<VersionCatalogsExtension>()
        .named("libs")

internal fun VersionCatalog.requiredVersion(alias: String): String =
    findVersion(alias).get().requiredVersion
