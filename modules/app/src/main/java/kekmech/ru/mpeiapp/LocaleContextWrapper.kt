package kekmech.ru.mpeiapp

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

/**
 * TODO: Remove in 1.4.0 release, default locale will be null (automatic)
 * TODO: Also remove in [kekmech.ru.library_network.device_id.DeviceLocaleProvider]
 */
private const val DEFAULT_LOCALE = "ru_RU"

object LocaleContextWrapper {

    @Suppress("MagicNumber")
    fun wrapContext(context: Context): Context {
        if (Build.VERSION.SDK_INT < 25) return context
        val savedLocale = createLocaleFromSharedPreferences(context) ?: return context

        val newConfig = Configuration(context.resources.configuration)
        newConfig.setLocale(savedLocale)
        return context.createConfigurationContext(newConfig)
    }

    fun updateResourcesV24(context: Context) {
        val locale = createLocaleFromSharedPreferences(context) ?: return
        Locale.setDefault(locale)
        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)
    }

    private fun createLocaleFromSharedPreferences(context: Context): Locale? {
        val prefs = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)
        val lang = prefs.getString("app_lang", DEFAULT_LOCALE)?.split("_") ?: return null
        val (language, country) = lang[0] to lang[1]
        return Locale(language, country)
    }
}
