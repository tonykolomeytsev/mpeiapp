package kekmech.ru.common_network.device_id

import android.content.Context
import java.util.*

class DeviceIdProvider(private val context: Context) {

    @Volatile
    private var deviceId: String? = null

    @Synchronized
    fun getDeviceId(): String {
        if (deviceId == null) {
            val sharedPrefs = context.getSharedPreferences(
                "pref_device_id",
                Context.MODE_PRIVATE
            )
            deviceId = sharedPrefs.getString("pref_device_id", null)
            if (deviceId == null) {
                deviceId = UUID.randomUUID().toString()
                sharedPrefs.edit()
                    .putString("pref_device_id", deviceId)
                    .apply()
            }
        }
        return deviceId!!
    }
}