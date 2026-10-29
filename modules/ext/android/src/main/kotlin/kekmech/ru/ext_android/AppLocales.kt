package kekmech.ru.ext_android

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * The language of the app is chosen in the settings ("ru_RU" / "en_US"), independent of the system language.
 *
 * - [apply] hands the language to AppCompat: it is applied to every AppCompat activity (also before the first frame
 *   of the splash screen) and the visible ones are recreated. On Android 13+ it goes through the system
 *   per-app language service, on older versions AppCompat stores it itself (`AppLocalesMetadataHolderService`).
 * - [localized] gives a context in the chosen language for code that has no activity at hand
 *   (notifications, widgets, receivers).
 */
public object AppLocales {

    private const val PREFS_NAME = "mpeix"
    private const val KEY_LANGUAGE = "app_lang"

    /** "ru_RU" -> "ru-RU" */
    public fun toLanguageTag(code: String): String = code.trim().replace('_', '-')

    public fun toLocale(code: String): Locale = Locale.forLanguageTag(toLanguageTag(code))

    /** The language saved by the settings, null while the user never chose one (then the system language is used) */
    public fun savedLanguageCode(context: Context): String? =
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, null)
            ?.takeIf { it.isNotBlank() }

    /** Applies the language to all screens. `null` / blank means "follow the system". Call on the main thread. */
    public fun apply(code: String?) {
        val locales = if (code.isNullOrBlank()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(toLanguageTag(code))
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }

    /** To be called as early as possible (Application.onCreate) */
    public fun applySaved(context: Context) {
        apply(savedLanguageCode(context))
    }

    /** [context] with the chosen language; [context] itself while the user never chose one */
    public fun localized(context: Context): Context {
        val code = savedLanguageCode(context) ?: return context
        val locale = toLocale(code)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    /** Locale to format dates and numbers with */
    public fun localeOf(context: Context): Locale =
        savedLanguageCode(context)?.let(::toLocale) ?: Locale.getDefault()
}
