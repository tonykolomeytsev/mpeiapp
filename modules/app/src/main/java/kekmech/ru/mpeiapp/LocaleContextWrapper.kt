package kekmech.ru.mpeiapp

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

object LocaleContextWrapper {

    @Suppress("MagicNumber")
    fun wrapContext(context: Context): Context {
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
        val lang = prefs.getString("app_lang", null)?.split("_") ?: return null
        val (language, country) = lang[0] to lang[1]
        return Locale(language, country)
    }
}
