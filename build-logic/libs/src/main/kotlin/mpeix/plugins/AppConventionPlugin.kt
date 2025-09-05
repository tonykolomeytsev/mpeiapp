package mpeix.plugins

import com.android.build.api.dsl.ApplicationExtension
import mpeix.plugins.ext.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import mpeix.plugins.setup.Plugins
import mpeix.plugins.features.android.configureKotlinAndroid
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import java.io.File

internal abstract class AppConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply(Plugins.AndroidApplication)
            apply(Plugins.KotlinAndroid)
            apply(Plugins.GradleAndroidCacheFix)
        }

        val config = getAndroidProjectConfiguration()
        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this, explicitApi = false)
            defaultConfig {
                setProguardFiles(projectProguardFiles() + getDefaultProguardFile("proguard-android-optimize.txt"))
            }

            packaging {
                resources.excludes += setOf(
                    "META-INF/DEPENDENCIES",
                    "META-INF/LICENSE*",
                    "META-INF/NOTICE*",
                    "META-INF/ASL2.0",
                    "META-INF/*.kotlin_module",
                )
                jniLibs.pickFirsts += "**/libc++_shared.so" // Для либ Сбера
            }

            with(buildFeatures) {
                compose = false
                viewBinding = false
                shaders = false
                resValues = true
                buildConfig = true
                aidl = false
            }
        }
        config.defaultDependencies.orNull?.execute(dependencies)
        config.defaultAndroidDependencies.orNull?.execute(dependencies)
    }

    private fun Project.projectProguardFiles(): List<File> =
        projectDir.resolve("proguard")
            .listFiles()
            .orEmpty()
            .filter { it.extension == "pro" }
}