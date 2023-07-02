package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Deprecated("Use `mpeix.android.library`, `mpeix.android.extension` " +
        "or `mpeix.feature.impl` instead")
@Suppress("unused")
internal class AndroidLibConventionPlugin : Plugin<Project> {

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
