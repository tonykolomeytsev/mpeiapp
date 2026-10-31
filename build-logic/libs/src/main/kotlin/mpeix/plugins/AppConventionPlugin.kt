package mpeix.plugins

import com.android.build.api.dsl.ApplicationExtension
import mpeix.plugins.ext.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import mpeix.plugins.setup.Plugins
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import mpeix.plugins.features.unittesting.setupUnitTests
import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import java.io.File

internal abstract class AppConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(plugins) {
            apply(Plugins.AndroidApplication)
            apply(Plugins.GradleAndroidCacheFix)
        }

        val config = getAndroidProjectConfiguration()
        extensions.configure<ApplicationExtension> {
            compileSdk = config.targetSdk.get()
            buildToolsVersion = config.buildToolsVersion.get()

            defaultConfig {
                minSdk = config.minSdk.get()
                targetSdk = config.targetSdk.get()
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

            testOptions {
                execution = "ANDROIDX_TEST_ORCHESTRATOR"
                unitTests.isReturnDefaultValues = true
            }

            compileOptions {
                isCoreLibraryDesugaringEnabled = true
                targetCompatibility = JavaVersion.VERSION_17
                sourceCompatibility = JavaVersion.VERSION_17
            }
        }
        config.defaultDependencies.orNull?.execute(dependencies)
        config.defaultAndroidDependencies.orNull?.execute(dependencies)
        setupUnitTests()
        project.extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
        project.dependencies.add(
            "coreLibraryDesugaring",
            "com.android.tools:desugar_jdk_libs:${config.coreLibraryDesugaringVersion.get()}"
        )
    }

    private fun Project.projectProguardFiles(): List<File> =
        projectDir.resolve("proguard")
            .listFiles()
            .orEmpty()
            .filter { it.extension == "pro" }
}