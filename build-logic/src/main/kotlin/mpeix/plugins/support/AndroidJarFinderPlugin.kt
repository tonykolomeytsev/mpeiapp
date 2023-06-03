package mpeix.plugins.support

import AndroidJarLocationKey
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * # `android.jar` finder plugin
 *
 * The solution was found in https://github.com/stepango/android-jar
 * and slightly modified to fit the needs of mpeix.
 *
 * ### Usage:
 *
 * Apply plugin in the root project:
 *
 * ```kotlin
 * plugins {
 *     id("mpeix.android-jar-finder")
 * }
 * ```
 *
 * Use `androidJar` variable in `dependencies` section in `build.gradle.kts` of
 * pure-kotlin subprojects:
 *
 * ```kotlin
 * dependencies {
 *     compileOnly(androidJar)
 * }
 * ```
 */
@Suppress("unused")
class AndroidJarFinderPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val libs = versionCatalog
            val androidJarFinder = AndroidJarFinder(target)
            val androidJar = androidJarFinder.find(libs.requiredVersion("compileSdk").toInt())
            extensions.extraProperties[AndroidJarLocationKey] = androidJar
        }
    }
}

private class AndroidJarFinder(private val project: Project) {

    fun find(compileSdk: Int): ConfigurableFileCollection =
        project.files("${findSdkLocation()}/platforms/android-${compileSdk}/android.jar")

    private fun findSdkLocation(): File {
        val localProperties = File(project.rootDir, "local.properties").takeIf { it.exists() }
        return localProperties?.let(::getSdkDirFromLocalProperties)
            ?: getSdkDirFromEnvVariable()
            ?: throw RuntimeException("The Android SDK location could not be found in either " +
                    "the `local.properties` file or the ANDROID_HOME environment variable")
    }

    private fun getSdkDirFromLocalProperties(localProperties: File): File? {
        val properties = Properties()
        FileInputStream(localProperties).use(properties::load)
        // `sdk.dir` is for absolute path, `android.dir` is for relative
        return properties.getProperty("sdk.dir")?.let(::File)
            ?: properties.getProperty("android.dir")?.let { File(project.rootDir, it) }
    }

    private fun getSdkDirFromEnvVariable(): File? {
        return System.getenv("ANDROID_HOME")?.let(::File)
            ?: System.getProperty("android.home")?.let(::File)
    }
}
