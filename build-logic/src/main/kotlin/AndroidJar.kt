import org.gradle.api.Project

internal const val AndroidJarLocationKey = "MPEIX_INTERNAL__ANDROID_JAR_LOCATION"

val Project.androidJar
    get() = rootProject.extensions.extraProperties.properties[AndroidJarLocationKey]
        ?: throw RuntimeException(
            "The property with `android.jar` location is missing. Make sure the " +
                    "`mpeix.android-jar-finder` plugin is applied in the root project"
        )
