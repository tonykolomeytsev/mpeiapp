package mpeix.plugins.dependencies

import mpeix.plugins.ext.requiredVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.ConfigurableFileCollection
import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * The solution was found in https://github.com/stepango/android-jar
 * and slightly modified to fit the needs of mpeix.
 */
@Suppress("unused")
class AndroidJarFinderPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val catalog =
            target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        val androidJar =
            AndroidJarFinder(target).find(catalog.requiredVersion("compileSdk").toInt())
        target.rootProject.extensions.extraProperties["androidJar"] = androidJar
    }
}


private class AndroidJarFinder(private val project: Project) {

    fun find(compileSdk: Int): ConfigurableFileCollection =
        project.files("${findSdkLocation()}/platforms/android-${compileSdk}/android.jar")

    private fun findSdkLocation(): File {
        val localProperties = File(project.rootDir, "local.properties")
        return getSdkDirFromLocalProperties(localProperties)
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
