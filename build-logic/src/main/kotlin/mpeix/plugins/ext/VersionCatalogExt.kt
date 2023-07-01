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

internal fun VersionCatalog.requiredVersion(alias: String): String {
    val version = findVersion(alias)
    if (version.isPresent) return version.get().requiredVersion
    error("Can't find version by alias `$alias` in versions catalog")
}
