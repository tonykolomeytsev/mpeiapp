package mpeix.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logging
import org.gradle.kotlin.dsl.configure

@Deprecated("Use `mpeix.android.library`, `mpeix.android.extension` " +
        "or `mpeix.feature.impl` instead")
@Suppress("unused")
internal class AndroidLibConventionPlugin : Plugin<Project> {

    private val logger = Logging.getLogger(javaClass)

    override fun apply(target: Project) {
        logger.log(LogLevel.WARN, "Plugin `mpeix.android.lib` is deprecated. " +
                "Use `mpeix.android.library`, `mpeix.android.extension` " +
                "or `mpeix.feature.impl` instead")
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
