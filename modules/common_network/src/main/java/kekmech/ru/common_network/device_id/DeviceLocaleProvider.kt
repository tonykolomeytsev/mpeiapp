package kekmech.ru.common_network.device_id

import android.content.SharedPreferences
import java.util.*

/**
 * TODO: Remove in 1.4, default locale will be null (automatic)
 * TODO: Also remove in [kekmech.ru.mpeiapp.LocaleContextWrapper]
 */
private const val DEFAULT_LOCALE = "ru_RU"

internal class DeviceLocaleProvider(
    private val sharedPreferences: SharedPreferences
) {

    fun getLanguage() =
        sharedPreferences.getString("app_lang", DEFAULT_LOCALE) ?: Locale.getDefault().language
}