package kekmech.ru.lib_network.device_id

import android.content.Context
import java.util.UUID

private const val PREF_DEVICE_ID = "pref_device_id"

// TODO: remove to separate module
class DeviceIdProvider(private val context: Context) {

    @Volatile
    private var deviceId: String? = null

    @Synchronized
    fun getDeviceId(): String {
        if (deviceId == null) {
            val sharedPrefs = context.getSharedPreferences(
                PREF_DEVICE_ID,
                Context.MODE_PRIVATE
            )
            deviceId = sharedPrefs.getString(PREF_DEVICE_ID, null)
            if (deviceId == null) {
                deviceId = UUID.randomUUID().toString()
                sharedPrefs.edit()
                    .putString(PREF_DEVICE_ID, deviceId)
                    .apply()
            }
        }
        return deviceId!!
    }
}
