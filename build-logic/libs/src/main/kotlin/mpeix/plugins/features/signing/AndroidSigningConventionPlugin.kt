package mpeix.plugins.features.signing

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import mpeix.plugins.ext.configure
import mpeix.plugins.ext.isCiBuild
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class AndroidSigningConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.configure<BaseAppModuleExtension> {
                with(signingConfigs.maybeCreate("release")) {
                    if (isCiBuild) {
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
}