package mpeix.plugins.features.signing

import com.android.build.api.dsl.ApplicationExtension
import mpeix.plugins.ext.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Плагин конвенции подписи релизных сборок приложения «МЭИ.Хаб».
 *
 * Ключ подписи и пароли НИКОГДА не хранятся в репозитории. Они берутся из переменных окружения
 * или из Gradle-свойств (например, из `~/.gradle/gradle.properties`):
 * - `SIGNING_KEYSTORE_PATH`  путь к хранилищу (абсолютный или от корня проекта);
 * - `SIGNING_STORE_PASSWORD` пароль хранилища (или `KEY_STORE_PASSWORD`);
 * - `SIGNING_KEY_ALIAS`      алиас ключа (или `KEY_ALIAS`);
 * - `SIGNING_KEY_PASSWORD`   пароль ключа (или `KEY_PASSWORD`), по умолчанию равен паролю хранилища.
 *
 * Если что-то из этого не задано, `release` подписывается стандартным debug-сертификатом,
 * чтобы сборка проходила; такой APK нельзя публиковать.
 */
@Suppress("unused")
internal class AndroidSigningConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        fun setting(vararg names: String): String? = names.firstNotNullOfOrNull { name ->
            System.getenv(name)?.takeIf { it.isNotBlank() }
                ?: (findProperty(name) as? String)?.takeIf { it.isNotBlank() }
        }

        extensions.configure<ApplicationExtension> {
            signingConfigs.maybeCreate("release").apply {
                val path = setting("SIGNING_KEYSTORE_PATH")
                val keystoreFile = path?.let { File(it).let { f -> if (f.isAbsolute) f else rootProject.file(it) } }
                val storePass = setting("SIGNING_STORE_PASSWORD", "KEY_STORE_PASSWORD")
                val alias = setting("SIGNING_KEY_ALIAS", "KEY_ALIAS")

                if (keystoreFile != null && keystoreFile.exists() && storePass != null && alias != null) {
                    storeFile = keystoreFile
                    storePassword = storePass
                    keyAlias = alias
                    keyPassword = setting("SIGNING_KEY_PASSWORD", "KEY_PASSWORD") ?: storePass
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
