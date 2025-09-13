package mpeix.plugins.features.secrets

import mpeix.plugins.ext.create
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("Unused")
internal abstract class SecretsPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        extensions.create<SecretsExtension>("secrets")
    }
}