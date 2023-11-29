package mpeix.plugins

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import mpeix.plugins.ext.requiredVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class BaseAndroidModulePlugin : Plugin<Project> {

    private val jvmTarget = JavaVersion.VERSION_11

    override fun apply(target: Project) {
        target.plugins.apply {
            apply("kotlin-android")
            apply("kotlin-parcelize")
        }

        val catalog = target.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        target.extensions.configure(BaseExtension::class.java) { extension ->
            extension.configure(
                minSdk = catalog.requiredVersion("minSdk").toInt(),
                targetSdk = catalog.requiredVersion("targetSdk").toInt(),
                buildTools = catalog.requiredVersion("buildTools"),
                appVersionName = catalog.requiredVersion("appVersionName"),
                appVersionCode = catalog.requiredVersion("appVersionCode").toInt(),
            )
        }
        target.tasks.withType(KotlinCompile::class.java) { task ->
            task.kotlinOptions.jvmTarget = jvmTarget.toString()
            // Allow use of @OptIn
            task.kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        }
        target.tasks.withType(Test::class.java) { task -> task.useJUnitPlatform() }
        target.dependencies.setupDependencies(catalog)
    }

    private fun DependencyHandler.setupDependencies(catalog: VersionCatalog) {
        catalog.findLibrary("desugaring").ifPresent { moduleProvider ->
            add("coreLibraryDesugaring", moduleProvider)
        }
    }

    @Suppress("UnstableApiUsage")
    private fun BaseExtension.configure(
        minSdk: Int,
        targetSdk: Int,
        buildTools: String,
        appVersionName: String,
        appVersionCode: Int,
    ) {
        setCompileSdkVersion(targetSdk)
        buildToolsVersion = buildTools
        defaultConfig { config ->
            config.minSdk = minSdk
            config.targetSdk = targetSdk
            config.versionName = appVersionName
            config.versionCode = appVersionCode
        }
        // add app version name as a BuildConfig fields
        buildTypes {
            it.configureEach { buildType ->
                buildType.buildConfigField(
                    type = "String",
                    name = "VERSION_NAME",
                    value = "\"$appVersionName\"",
                )
            }
        }
        compileOptions { options ->
            options.isCoreLibraryDesugaringEnabled = true
            options.targetCompatibility = jvmTarget
            options.sourceCompatibility = jvmTarget
        }
    }
}