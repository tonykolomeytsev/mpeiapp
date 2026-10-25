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
 * 1. Если задан `SIGNING_KEYSTORE_PATH` (или CI положил `keystore.jks` и задан `KEY_STORE_PASSWORD`),
 *    используется это хранилище и пароли из переменных окружения CI/CD.
 * 2. Иначе используется ключ проекта `keystore/mpei_hub_release.jks` (RSA 4096, alias `mpei_hub`),
 *    так что `assembleRelease` подписывается им без дополнительной настройки.
 * 3. Если ключ не найден, откатывается к стандартному debug-сертификату для сохранения успешности сборки.
 */
@Suppress("unused")
internal class AndroidSigningConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        extensions.configure<ApplicationExtension> {
            val releaseSigning = signingConfigs.maybeCreate("release").apply {
                val projectKeystore = rootProject.file(PROJECT_KEYSTORE)
                val ciKeystore = rootProject.file(CI_KEYSTORE)
                val customKeystorePath = System.getenv("SIGNING_KEYSTORE_PATH")
                val keystoreFile = if (!customKeystorePath.isNullOrBlank()) {
                    val customFile = File(customKeystorePath)
                    if (customFile.isAbsolute) customFile else rootProject.file(customKeystorePath)
                } else if (ciKeystore.exists() && !System.getenv("KEY_STORE_PASSWORD").isNullOrBlank()) {
                    ciKeystore
                } else if (projectKeystore.exists()) {
                    projectKeystore
                } else {
                    null
                }

                if (keystoreFile != null && keystoreFile.exists()) {
                    storeFile = keystoreFile
                    storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                        ?: System.getenv("KEY_STORE_PASSWORD")
                        ?: PROJECT_KEY_PASSWORD
                    keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                        ?: System.getenv("KEY_ALIAS")
                        ?: PROJECT_KEY_ALIAS
                    keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                        ?: System.getenv("KEY_PASSWORD")
                        ?: PROJECT_KEY_PASSWORD
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

    private companion object {
        const val PROJECT_KEYSTORE = "keystore/mpei_hub_release.jks"
        const val CI_KEYSTORE = "keystore.jks"
        const val PROJECT_KEY_ALIAS = "mpei_hub"

        // storePassword == keyPassword (PKCS12 keystore)
        const val PROJECT_KEY_PASSWORD = "REMOVED"
    }
}
