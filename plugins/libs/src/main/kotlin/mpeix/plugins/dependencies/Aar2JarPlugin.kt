package mpeix.plugins.dependencies

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
import org.gradle.plugins.ide.idea.model.IdeaModel
import java.io.FileOutputStream
import java.util.zip.ZipFile

@Suppress("unused")
class Aar2JarPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val compileOnlyAar = target.configurations.register("compileOnlyAar")
        val testImplementationAar = target.configurations.register("testImplementationAar")

        registerConfigurationsInIdea(target, testImplementationAar, compileOnlyAar)

        target.dependencies.registerTransform(Aar2JarTransformAction::class.java) { spec ->
            spec.from.attribute(ARTIFACT_TYPE_ATTRIBUTE, "aar")
            spec.to.attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_TYPE)
        }

        compileOnlyAar.configure { config ->
            config.isCanBeConsumed = false
            config.baseConfiguration(target, "main") {
                compileClasspath += config
            }
        }

        testImplementationAar.configure { config ->
            config.isCanBeConsumed = false
            config.baseConfiguration(target, "test") {
                compileClasspath += config
                runtimeClasspath += config
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

    private fun registerConfigurationsInIdea(
        project: Project,
        testImplementationAarAsJar: NamedDomainObjectProvider<Configuration>,
        compileOnlyAarAsJar: NamedDomainObjectProvider<Configuration>,
    ) {
        project.pluginManager.withPlugin("idea") {
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


