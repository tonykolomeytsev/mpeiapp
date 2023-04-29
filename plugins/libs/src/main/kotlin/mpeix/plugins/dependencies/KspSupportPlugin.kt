package mpeix.plugins.dependencies

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import java.io.File

@Suppress("unused")
class KspSupportPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply {
            apply("com.google.devtools.ksp")
        }
        target.kotlinExtension.provideKspSourceSets()
        target.extensions.getByType(KspExtension::class.java).apply {
            configureRoom(target.projectDir)
        }
    }

    private fun KotlinProjectExtension.provideKspSourceSets() {
        sourceSets.named("main").configure {
            it.kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
        sourceSets.named("test").configure {
            it.kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }

    private fun KspExtension.configureRoom(projectDir: File) {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
    }
}
