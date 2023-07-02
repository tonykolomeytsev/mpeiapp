package mpeix.plugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Deprecated("Use `mpeix.feature.api`, `mpeix.kotlin.library` or " +
        "`mpeix.kotlin.extension` instead")
@Suppress("unused")
internal class KotlinLibConventionPlugin : Plugin<Project> {

    private val logger = Logging.getLogger(javaClass)
    private val jvmTarget = JavaVersion.VERSION_11

    override fun apply(target: Project) {
        logger.log(LogLevel.WARN, "Plugin `mpeix.kotlin.lib` is deprecated. " +
                "Use `mpeix.feature.api`, `mpeix.kotlin.library` or " +
                "`mpeix.kotlin.extension` instead")
        with(target) {
            with(plugins) {
                apply("kotlin")
                apply("mpeix.kotlin.aar2jar")
                apply("mpeix.detekt")
            }
            tasks.withType<Test> {
                useJUnitPlatform()
            }
            tasks.withType<JavaCompile> {
                sourceCompatibility = jvmTarget.toString()
                targetCompatibility = jvmTarget.toString()
            }
            tasks.withType<KotlinCompile> {
                kotlinOptions.jvmTarget = jvmTarget.toString()
            }
        }
    }
}
