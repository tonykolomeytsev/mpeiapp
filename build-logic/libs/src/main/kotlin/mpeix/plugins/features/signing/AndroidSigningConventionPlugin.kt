package mpeix.plugins.features.signing

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import mpeix.plugins.ext.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

@Suppress("unused")
internal class AndroidSigningConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.configure<BaseAppModuleExtension> {
                with(signingConfigs.maybeCreate("release")) {
                    val defaultKeystore = rootProject.file("keystore/release.keystore")
                    val legacyKeystore = rootProject.file("keystore.jks")
                    val customKeystorePath = System.getenv("SIGNING_KEYSTORE_PATH")
                    val keystoreFile = if (!customKeystorePath.isNullOrBlank()) {
                        val customFile = File(customKeystorePath)
                        if (customFile.isAbsolute) customFile else rootProject.file(customKeystorePath)
                    } else if (defaultKeystore.exists()) {
                        defaultKeystore
                    } else if (legacyKeystore.exists()) {
                        legacyKeystore
                    } else {
                        null
                    }

                    if (keystoreFile != null && keystoreFile.exists()) {
                        storeFile = keystoreFile
                        storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                            ?: System.getenv("KEY_STORE_PASSWORD")
                            ?: "REMOVED"
                        keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                            ?: System.getenv("KEY_ALIAS")
                            ?: "mpeihub"
                        keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                            ?: System.getenv("KEY_PASSWORD")
                            ?: "REMOVED"
                    } else {
                        initWith(signingConfigs.getByName("debug"))
                    }
                }
            }
        }
    }
}