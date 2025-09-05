package mpeix.plugins.features.androidjar

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Provider
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import java.util.Properties
import kotlin.io.path.Path

internal const val AndroidJarLocationKey = "MPEIX_INTERNAL__ANDROID_JAR_LOCATION"

internal fun Project.enableCompileOnlyAndroidJar(compileSdk: Provider<Int>) {
    val androidJarFinder = AndroidJarFinder(rootProject)
    val androidJar = androidJarFinder.find(compileSdk.get())
    rootProject.extensions.extraProperties[AndroidJarLocationKey] = androidJar
}

/**
 * A class that encapsulates the logic for finding android.jar in the system
 * @see find
 */
private class AndroidJarFinder(private val project: Project) {

    /** Find android.jar for the required SDK version */
    fun find(compileSdk: Int): ConfigurableFileCollection =
        project.files(
            project.provider {
                "${findSdkLocation()}/platforms/android-${compileSdk}/android.jar"
            },
        )

    private fun findSdkLocation(): Path {
        val localProperties = File(project.rootDir, "local.properties").takeIf { it.exists() }
        return localProperties?.let(::getSdkDirFromLocalProperties)
            ?: getSdkDirFromEnvVariable()
            ?: throw RuntimeException(
                "The Android SDK location could not be found in following locations:" +
                        "- Absolute path `sdk.dir` in `local.properties` file\n" +
                        "- Relative path `android.dir` in `local.properties` file\n" +
                        "- `ANDROID_HOME` environment variable\n" +
                        "- System property `android.home`",
            )
    }

    private fun getSdkDirFromLocalProperties(localProperties: File): Path? {
        val properties = Properties()
        FileInputStream(localProperties).use(properties::load)
        // `sdk.dir` is for absolute path, `android.dir` is for relative
        return properties.getProperty("sdk.dir")?.let(::Path)
            ?: properties.getProperty("android.dir")
                ?.let { project.rootDir.resolve(it).toPath() }
    }

    private fun getSdkDirFromEnvVariable(): Path? {
        return System.getenv("ANDROID_HOME")?.let(::Path)
            ?: System.getProperty("android.home")?.let(::Path)
    }
}
