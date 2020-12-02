package kekmech.ru.mpeiapp

import android.content.Context
import android.content.res.Configuration
import java.util.*

class LocaleContextWrapper {

    fun wrapContext(context: Context): Context {
        val savedLocale = createLocaleFromSharedPreferences(context)!!
           // ?: return context
        Locale.setDefault(savedLocale)
        val newConfig = Configuration()
        newConfig.setLocale(savedLocale)
        return context.createConfigurationContext(newConfig)
    }

    private fun createLocaleFromSharedPreferences(context: Context): Locale? {
        return Locale("en", "RU")
    }
}