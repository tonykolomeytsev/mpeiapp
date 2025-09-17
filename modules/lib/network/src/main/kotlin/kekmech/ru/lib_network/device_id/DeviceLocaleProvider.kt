package kekmech.ru.lib_network.device_id

import android.content.SharedPreferences
import java.util.Locale

internal class DeviceLocaleProvider(
    private val sharedPreferences: SharedPreferences
) {

    fun getLanguage(): String {
        val languageCode = sharedPreferences.getString("app_lang", null)?.split('_')
            ?: return Locale.getDefault().language
        return languageCode[0]
    }
}
