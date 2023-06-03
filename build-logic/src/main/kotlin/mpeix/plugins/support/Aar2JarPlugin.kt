package mpeix.plugins.support

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JAR_TYPE
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.registerTransform
import org.gradle.plugins.ide.idea.model.IdeaModel
import java.io.FileOutputStream
import java.util.zip.ZipFile

/**
 * # AAR to JAR transformation plugin
 *
 * The solution was found in https://github.com/stepango/aar2jar
 * and slightly modified to fit the needs of mpeix.
 *
 * ### Usage:
 *
 * This plugin is automatically applied for all subprojects with "mpeix.kotlin" plugin.
 *
 * You can use `compileOnlyAar` configuration in `dependencies` section in `build.gradle.kts` of
 * pure-kotlin subprojects:
 *
 * ```kotlin
 * dependencies {
 *     compileOnlyAar(libs.androidx.fragment)
 * }
 * ```
 */
@Suppress("unused")
class Aar2JarPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val compileOnlyAar = configurations.register("compileOnlyAar")
            val testImplementationAar = configurations.register("testImplementationAar")

            registerConfigurationsInIdea(
                testImplementationAarAsJar = testImplementationAar,
                compileOnlyAarAsJar = compileOnlyAar,
            )

            dependencies.registerTransform(Aar2JarTransformAction::class) { spec ->
                spec.from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "aar")
                spec.to.attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_TYPE)
            }

            compileOnlyAar.configure { config ->
                config.isCanBeConsumed = false
                config.baseConfiguration(project = target, sourceSetName = "main") {
                    compileClasspath += config
                }
            }
            testImplementationAar.configure { config ->
                config.isCanBeConsumed = false
                config.baseConfiguration(project = target, sourceSetName = "test") {
                    compileClasspath += config
                    runtimeClasspath += config
                }
            }
        }
    }

    private fun Configuration.baseConfiguration(
        project: Project,
        sourceSetName: String,
        action: SourceSet.() -> Unit,
    ) {
        isTransitive = false
        attributes { container ->
            container.attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_TYPE)
        }
        project.pluginManager.withPlugin("java") {
            val sourceSets =
                project.extensions.getByType(JavaPluginExtension::class.java).sourceSets
            sourceSets.named(sourceSetName, action)
        }
    }

    private fun Project.registerConfigurationsInIdea(
        testImplementationAarAsJar: NamedDomainObjectProvider<Configuration>,
        compileOnlyAarAsJar: NamedDomainObjectProvider<Configuration>,
    ) {
        pluginManager.withPlugin("idea") {
            val scopes = project.extensions
                .getByType(IdeaModel::class.java)
                .module
                .scopes

            scopes["TEST"]
                ?.get("plus")
                ?.apply { add(testImplementationAarAsJar.get()) }

            scopes.forEach {
                it.value["plus"]
                    ?.apply { add(compileOnlyAarAsJar.get()) }
            }
        }
    }

    abstract class Aar2JarTransformAction : TransformAction<TransformParameters.None> {

        @get:InputArtifact
        @get:PathSensitive(PathSensitivity.RELATIVE)
        abstract val inputArtifact: Provider<FileSystemLocation>

        override fun transform(outputs: TransformOutputs) {
            val inputFile = inputArtifact.get().asFile
            val outputFile = outputs.file(inputFile.name.replace(".aar", ".jar"))
            ZipFile(inputFile).use { zipFile ->
                zipFile
                    .entries()
                    .toList()
                    .first { entry -> entry.name == "classes.jar" }
                    .let(zipFile::getInputStream)
                    .use { input ->
                        FileOutputStream(outputFile).use { output -> input.copyTo(output) }
                    }
            }
        }
    }
}


