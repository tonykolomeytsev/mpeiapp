package mpeix.plugins.setup

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.disallowProjectDependencies(reason: () -> String) {
    dependencies {
        configurations.all { configuration ->
            configuration.resolutionStrategy.failOnNonReproducibleResolution()
            configuration.resolutionStrategy.eachDependency { details ->
                // Here details.requested.group always equals root project name if the current
                // dependency is a subproject. But test tasks require current project itself
                // as dependency, hence we allow it
                if (details.requested.group == rootProject.name &&
                        details.requested.name != name) {
                    error(details.requested.toString() + "\n" + reason.invoke())
                }
            }
        }
    }
}
