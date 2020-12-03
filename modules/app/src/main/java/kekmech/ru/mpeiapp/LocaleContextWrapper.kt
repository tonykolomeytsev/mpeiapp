package kekmech.ru.mpeiapp

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

class LocaleContextWrapper {

    fun wrapContext(context: Context): Context {
        val savedLocale = createLocaleFromSharedPreferences(context) ?: return context

        val newConfig = Configuration(context.resources.configuration)
        newConfig.setLocale(savedLocale)
        if (Build.VERSION.SDK_INT < 25) {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(newConfig, context.resources.displayMetrics)
            return context
        } else {
            return context.createConfigurationContext(newConfig)
        }
    }

    private fun createLocaleFromSharedPreferences(context: Context): Locale? {
        val prefs = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)
        val lang = prefs.getString("app_lang", null)?.split("_") ?: return null
        val (language, country) = lang[0] to lang[1]
        return Locale(language, country)
    }
}