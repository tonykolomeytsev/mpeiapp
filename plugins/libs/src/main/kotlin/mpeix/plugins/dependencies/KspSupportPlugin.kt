package mpeix.plugins.dependencies

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

@Suppress("unused")
class KspSupportPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply {
            apply("com.google.devtools.ksp")
        }
        target.kotlinExtension.apply {
            sourceSets.named("main").configure {
                it.kotlin.srcDir("build/generated/ksp/main/kotlin")
            }
            sourceSets.named("test").configure {
                it.kotlin.srcDir("build/generated/ksp/test/kotlin")
            }
        }
    }
}
