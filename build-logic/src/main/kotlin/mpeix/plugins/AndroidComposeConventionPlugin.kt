package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import mpeix.plugins.setup.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
internal class AndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this, fullDependencySet = false)

                @Suppress("UnstableApiUsage")
                buildFeatures {
                    compose = true
                }
            }
        }
    }
}
