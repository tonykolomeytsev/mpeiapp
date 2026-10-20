package mpeix.plugins.features.signing

import com.android.build.api.dsl.ApplicationExtension
import mpeix.plugins.ext.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Плагин конвенции подписи релизных сборок приложения «МЭИ.Хаб».
 *
 * Применяет единые правила подписания APK / AAB:
 * 1. Проверяет наличие переменных окружения CI/CD (SIGNING_KEYSTORE_PATH, SIGNING_STORE_PASSWORD и др.).
 * 2. При их отсутствии выполняет безопасный фоллбэк к локальному хранилищу ключей проекта:
 *    - `keystore/release.keystore` (официальный ключ МЭИ.Хаб, RSA 2048)
 *    - `keystore.jks` (исторический ключ)
 * 3. Если ключ не найден, откатывается к стандартному debug-сертификату для сохранения успешности сборки.
 */
@Suppress("unused")
internal class AndroidSigningConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        extensions.configure<ApplicationExtension> {
            val releaseSigning = signingConfigs.maybeCreate("release").apply {
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
                    val debugConfig = signingConfigs.getByName("debug")
                    storeFile = debugConfig.storeFile
                    storePassword = debugConfig.storePassword
                    keyAlias = debugConfig.keyAlias
                    keyPassword = debugConfig.keyPassword
                }
            }
        }
    }
}
