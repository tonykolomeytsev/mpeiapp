package kekmech.ru.mpeiapp

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleContextWrapper {

    @Suppress("MagicNumber")
    fun wrapContext(context: Context): Context {
        val savedLocale = createLocaleFromSharedPreferences(context) ?: return context
        val newConfig = Configuration(context.resources.configuration)
        newConfig.setLocale(savedLocale)
        return context.createConfigurationContext(newConfig)
    }

    private fun createLocaleFromSharedPreferences(context: Context): Locale? {
        val prefs = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)
        val lang = prefs.getString("app_lang", null)?.split("_") ?: return null
        val (language, country) = lang[0] to lang[1]
        @Suppress("DEPRECATION")
        return Locale(language, country)
    }
}
