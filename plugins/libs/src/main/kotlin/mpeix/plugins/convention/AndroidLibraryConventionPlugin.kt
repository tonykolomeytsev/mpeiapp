package mpeix.plugins.convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
internal class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply("com.android.library")
                apply("mpeix.android.base")
            }

            extensions.configure<LibraryExtension> {
                namespace = "kekmech.ru.${target.name}"
            }
        }
    }
}
