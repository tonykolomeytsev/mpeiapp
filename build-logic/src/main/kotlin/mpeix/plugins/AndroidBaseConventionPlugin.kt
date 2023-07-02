package mpeix.plugins

import com.android.build.gradle.BaseExtension
import mpeix.plugins.ext.coreLibraryDesugaring
import mpeix.plugins.ext.implementation
import mpeix.plugins.ext.requiredVersion
import mpeix.plugins.ext.versionCatalog
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Deprecated("")
@Suppress("unused")
internal class AndroidBaseConventionPlugin : Plugin<Project> {

    private val jvmTarget = JavaVersion.VERSION_11

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply("kotlin-android")
                apply("kotlin-parcelize")
                apply("org.gradle.android.cache-fix")
                apply("mpeix.detekt")
            }

            val libs = versionCatalog
            extensions.configure<BaseExtension> {
                configure(
                    minSdk = libs.requiredVersion("minSdk").toInt(),
                    compileSdk = libs.requiredVersion("compileSdk").toInt(),
                    targetSdk = libs.requiredVersion("targetSdk").toInt(),
                    buildTools = libs.requiredVersion("buildTools"),
                    appVersionName = libs.requiredVersion("appVersionName"),
                    appVersionCode = libs.requiredVersion("appVersionCode").toInt(),
                )
                with(buildFeatures) {
                    shaders = false
                }
            }

            tasks.withType<KotlinCompile> {
                kotlinOptions.jvmTarget = jvmTarget.toString()
                // Allow use of @OptIn
                kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            }
            tasks.withType<Test> {
                useJUnitPlatform()
            }

            dependencies {
                coreLibraryDesugaring(libs.findLibrary("desugaring").get())
                implementation(libs.findLibrary("timber").get())
            }
        }
    }

    private fun BaseExtension.configure(
        minSdk: Int,
        compileSdk: Int,
        targetSdk: Int,
        buildTools: String,
        appVersionName: String,
        appVersionCode: Int,
    ) {
        setCompileSdkVersion(compileSdk)
        buildToolsVersion = buildTools
        defaultConfig { config ->
            config.minSdk = minSdk
            config.targetSdk = targetSdk
            config.versionName = appVersionName
            config.versionCode = appVersionCode
            config.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        testOptions { options ->
            options.unitTests.isReturnDefaultValues = true
            options.unitTests.all { test -> test.useJUnitPlatform() }
            options.execution = "ANDROIDX_TEST_ORCHESTRATOR"
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
