package mpeix.plugins.features.globalconfig

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Property
import java.io.Serializable

abstract class AndroidProjectConfigurationExtension : Serializable {

    abstract val targetSdk: Property<Int>
    abstract val minSdk: Property<Int>
    abstract val buildToolsVersion: Property<String>
    abstract val coreLibraryDesugaringVersion: Property<String>

    abstract val defaultComposeDependencies: Property<Action<DependencyHandler>>
    fun defaultComposeDependencies(action: Action<DependencyHandler>) =
        defaultComposeDependencies.set(action)

    abstract val defaultAndroidDependencies: Property<Action<DependencyHandler>>
    fun defaultAndroidDependencies(action: Action<DependencyHandler>) =
        defaultAndroidDependencies.set(action)

    abstract val defaultDependencies: Property<Action<DependencyHandler>>
    fun defaultDependencies(action: Action<DependencyHandler>) =
        defaultDependencies.set(action)

    internal fun finalize() {
        require(targetSdk.isPresent) { "Property `targetSdk` must be set via `androidProjectConfiguration` extension in the root project" }
        targetSdk.finalizeValue()
        require(minSdk.isPresent) { "Property `minSdk` must be set via `androidProjectConfiguration` extension in the root project" }
        minSdk.finalizeValue()
        require(buildToolsVersion.isPresent) { "Property `buildToolsVersion` must be set via `androidProjectConfiguration` extension in the root project" }
        buildToolsVersion.finalizeValue()
        require(coreLibraryDesugaringVersion.isPresent) { "Property `coreLibraryDesugaringVersion` must be set via `androidProjectConfiguration` extension in the root project" }
        coreLibraryDesugaringVersion.finalizeValue()
    }
}

internal fun Project.registerAndroidProjectConfigurationExtension() {
    assert(project == rootProject) {
        "Extension `androidProjectConfiguration` can be registered only in the root project"
    }
    val ext = extensions.create(
        /* name = */ "androidProjectConfiguration",
        /* type = */ AndroidProjectConfigurationExtension::class.java
    )
    afterEvaluate { ext.finalize() }
}

internal fun Project.getAndroidProjectConfiguration(): AndroidProjectConfigurationExtension =
    rootProject.extensions.getByType(AndroidProjectConfigurationExtension::class.java)
