package kekmech.ru.mainscreen

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.core.UpdateChecker

class ForceUpdateChecker constructor(
    private val context: Context
) : UpdateChecker {

    override fun check(onUpdateNeededListener: (String, String) -> Unit) {
        //if (BuildConfig.DEBUG) return
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        Log.d(TAG, remoteConfig.toString())

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            val currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION)
            val appVersion = getAppVersion(context)
            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)
            val description = remoteConfig.getString(KEY_UPDATE_DESCRIPTION)

            Log.d(TAG, "$currentVersion $appVersion $description")

            if (currentVersion != appVersion) {
                onUpdateNeededListener(updateUrl, description)
            }
        }
    }

    private fun getAppVersion(context: Context): String {
        var result = ""

        try {
            result = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message ?: "")
        }

        return result
    }

    companion object {

        private val TAG = ForceUpdateChecker::class.java.simpleName

        val KEY_UPDATE_REQUIRED = "force_update_required"
        val KEY_CURRENT_VERSION = "force_update_version"
        val KEY_UPDATE_URL = "force_update_url"
        val KEY_UPDATE_DESCRIPTION = "force_update_description"

        fun with(context: Context): ForceUpdateChecker {
            return ForceUpdateChecker(context)
        }
    }
}