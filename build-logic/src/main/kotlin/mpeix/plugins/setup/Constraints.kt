package mpeix.plugins.setup

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.disallowProjectDependencies(reason: () -> String) {
    dependencies {
        configurations.all { configuration ->
            configuration.resolutionStrategy.failOnNonReproducibleResolution()
            configuration.resolutionStrategy.eachDependency { details ->
                if (details.requested.group == rootProject.name) {
                    error(reason.invoke())
                }
            }
        }
    }
}
