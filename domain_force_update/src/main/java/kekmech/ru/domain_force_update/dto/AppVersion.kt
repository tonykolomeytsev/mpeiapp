package kekmech.ru.domain_force_update.dto

data class AppVersion(
    val major: Int,
    val minor: Int,
    val build: Int
) {

    constructor(versionName: String) : this(
        major = getMajor(versionName),
        minor = getMinor(versionName),
        build = getBuild(versionName)
    )

    operator fun compareTo(other: AppVersion): Int = when {
        other.major != major -> major - other.major
        other.minor != minor -> minor - other.minor
        other.build != build -> build - other.build
        else -> 0
    }

    companion object {
        private fun getMajor(versionName: String): Int =
            versionName.clear().substringBefore('.').toIntOrNull() ?: 0

        private fun getMinor(versionName: String): Int =
            versionName.clear().substringAfter('.').substringBefore('.').toIntOrNull() ?: 0

        private fun getBuild(versionName: String): Int =
            versionName.clear().substringAfterLast('.').toIntOrNull() ?: 0

        private fun String.clear() =
            replace("[^0-9.]".toRegex(), "")
    }
}