package mpeix.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AndroidModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply {
            apply("com.android.library")
            apply("kotlin-android")
            apply("kotlin-parcelize")
            apply("mpeix.android.base")
        }
    }
}