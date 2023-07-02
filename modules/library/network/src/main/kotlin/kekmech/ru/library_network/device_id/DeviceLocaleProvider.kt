package kekmech.ru.library_network.device_id

import android.content.SharedPreferences
import java.util.Locale

/**
 * TODO: Remove in 1.4, default locale will be null (automatic)
 * TODO: Also remove in [kekmech.ru.mpeiapp.LocaleContextWrapper]
 */
private const val DEFAULT_LOCALE = "ru_RU"

internal class DeviceLocaleProvider(
    private val sharedPreferences: SharedPreferences
) {

    fun getLanguage(): String {
        val languageCode = sharedPreferences.getString("app_lang", DEFAULT_LOCALE)?.split('_')
            ?: return Locale.getDefault().language
        return languageCode[0]
    }
}
