package mpeix.plugins.features.aar2jar

import com.android.build.gradle.internal.dependency.AarTransform
import com.android.build.gradle.internal.dependency.ExtractAarTransform
import com.android.build.gradle.options.BooleanOption
import mpeix.plugins.ext.configure
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import com.android.build.gradle.internal.publishing.AndroidArtifacts.ArtifactType.AAR as Aar
import com.android.build.gradle.internal.publishing.AndroidArtifacts.ArtifactType.EXPLODED_AAR as ExplodedAar
import com.android.build.gradle.internal.publishing.AndroidArtifacts.ArtifactType.JAR as Jar
import com.android.build.gradle.internal.publishing.AndroidArtifacts.ArtifactType.PROCESSED_JAR as ProcessedJar
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE as ArtifactTypeAttribute

internal fun Project.registerAarToJarTransform() {
    registerTransformActions()
    registerConfigurations()
}

private fun Project.registerTransformActions() {
    // AAR -> Unpacked AAR
    dependencies.registerTransform(ExtractAarTransform::class.java) { spec ->
        spec.from.attribute(ArtifactTypeAttribute, Aar.type)
        spec.to.attribute(ArtifactTypeAttribute, ExplodedAar.type)
    }
    // Unpacked AAR -> JARs
    dependencies.registerTransform(AarTransform::class.java) { spec ->
        spec.from.attribute(ArtifactTypeAttribute, ExplodedAar.type)
        spec.to.attribute(ArtifactTypeAttribute, Jar.type)

        val sharedLibSupport = providers
            .gradleProperty(BooleanOption.CONSUME_DEPENDENCIES_AS_SHARED_LIBRARIES.propertyName)
            .map { it.toBoolean() }
            .orElse(BooleanOption.CONSUME_DEPENDENCIES_AS_SHARED_LIBRARIES.defaultValue)

        spec.parameters { par ->
            // AGP distinguishes between "JAR" and "Processed JAR". Externally, they are the same (.jar files),
            // but AGP considers that a regular external JAR may require desugaring, running Jetifier,
            // and other processing operations before it can be safely added
            // to the project's classpath.
            // All JARs located in an AAR are considered "Processed" by default and are immediately ready
            // for inclusion in the classpath. Therefore, AarTransform cannot unpack into Jar,
            // you need to specify unpacking directly into ProcessedJar.
            par.targetType.set(ProcessedJar)
            par.namespacedSharedLibSupport.set(sharedLibSupport)
        }
    }
}

private fun Project.registerConfigurations() {
    // "Declared" configurations used to specify dependencies in the dependencies block.
    // They don't do anything on their own; they are just needed as a scope for dependencies.
    // https://docs.gradle.org/current/userguide/declaring_dependencies_adv.html#sec:resolvable-consumable-configs
    val androidApiCompileOnly = registerDependencyConfiguration("aarCompileOnly")
    val androidApiTestImplementation = registerDependencyConfiguration("testAarImplementation")

    // "Resolvable" configurations, through which you can get the final set of files to be included
    // as dependencies.
    val aarCompileClasspath = registerResolvableConfiguration("aarCompileClasspath") {
        extendsFrom(androidApiCompileOnly.get())
    }
    val testAarCompileClasspath = registerResolvableConfiguration("testAarCompileClasspath") {
        extendsFrom(aarCompileClasspath.get(), androidApiTestImplementation.get())
    }
    val testAarRuntimeClasspath = registerResolvableConfiguration("testAarRuntimeClasspath") {
        extendsFrom(androidApiTestImplementation.get())
    }
    // Special configuration needed for connecting platform/bom dependencies.
    // Such dependencies require transitivity, but we cannot enable transitivity
    // for other configurations: https://github.com/gradle/gradle/issues/10195
    val transitiveAarCompileClasspath = registerResolvableConfiguration("transitiveAarCompileClasspath") {
        isTransitive = true
    }

    extensions.configure<JavaPluginExtension> {
        sourceSets.named("main") { main ->
            main.compileClasspath += aarCompileClasspath.get() + transitiveAarCompileClasspath.get()
        }
        sourceSets.named("test") { test ->
            test.compileClasspath += aarCompileClasspath.get() + testAarCompileClasspath.get() + transitiveAarCompileClasspath.get()
            test.runtimeClasspath += testAarRuntimeClasspath.get()
        }
    }

    // Register configuration in IDEA if needed
    plugins.withId("idea") {
        extensions.configure<IdeaModel> {
            module.scopes.forEach { (_, value) ->
                value["plus"]?.apply {
                    add(aarCompileClasspath.get())
                    add(transitiveAarCompileClasspath.get())
                }
            }
            module.scopes["TEST"]?.get("plus")?.add(testAarCompileClasspath.get())
        }
    }
}

@Suppress("UnstableApiUsage")
private fun Project.registerDependencyConfiguration(
    name: String,
    action: (Configuration.() -> Unit)? = null,
): NamedDomainObjectProvider<Configuration> =
    configurations.register(name) { conf ->
        with(conf) {
            isTransitive = false
            isCanBeDeclared = true
            isCanBeConsumed = false
            isCanBeResolved = false
        }
        action?.invoke(conf)
    }

@Suppress("UnstableApiUsage")
private fun Project.registerResolvableConfiguration(
    name: String,
    action: (Configuration.() -> Unit)? = null,
): NamedDomainObjectProvider<Configuration> =
    configurations.register(name) { conf ->
        with(conf) {
            isTransitive = false
            isCanBeDeclared = false
            isCanBeConsumed = false
            isCanBeResolved = true
            attributes
                .attribute(ArtifactTypeAttribute, Jar.type)
                .attribute(KotlinPlatformType.attribute, KotlinPlatformType.androidJvm)
        }
        action?.invoke(conf)
    }