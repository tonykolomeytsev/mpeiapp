package mpeix.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.SigningConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
internal class AndroidSigningConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(extensions.getByType<BaseAppModuleExtension>()) {
                with(signingConfigs.maybeCreate("release")) {
                    if (project.hasProperty("ci")) {
                        storeFile = rootProject.file("keystore.jks")
                        storePassword = System.getenv("KEY_STORE_PASSWORD")
                        keyAlias = System.getenv("KEY_ALIAS")
                        keyPassword = System.getenv("KEY_PASSWORD")
                    } else {
                        initWith(signingConfigs.getByName("debug"))
                    }
                }
            }
        }
    }

    private fun NamedDomainObjectContainer<SigningConfig>.configure(
        name: String,
        config: SigningConfig.() -> Unit,
    ) = named(name).configure(config)
}
