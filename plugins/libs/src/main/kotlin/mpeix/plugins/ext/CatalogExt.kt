package mpeix.plugins.ext

import org.gradle.api.artifacts.VersionCatalog

internal fun VersionCatalog.requiredVersion(alias: String): String =
    findVersion(alias).get().requiredVersion