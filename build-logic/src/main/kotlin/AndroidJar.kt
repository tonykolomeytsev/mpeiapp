import org.gradle.api.Project

internal const val AndroidJarLocationKey = "MPEIX_INTERNAL__ANDROID_JAR_LOCATION"

/**
 * Dependency notation for the android.jar library used in android subprojects.
 * Allows you to use android entities API in pure-kotlin module.
 * Available for use in pure-kotlin subprojects via the `compileOnly` configuration:
 *
 * ```kotlin
 * dependencies {
 *     compileOnly(androidJsr)
 * }
 * ```
 */
val Project.androidJar
    get() = rootProject.extensions.extraProperties.properties[AndroidJarLocationKey]
        ?: throw RuntimeException(
            "The property with `android.jar` location is missing. Make sure the " +
                    "`mpeix.android-jar-finder` plugin is applied in the root project"
        )
