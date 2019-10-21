package kekmech.ru.mainscreen

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.core.UpdateChecker
import javax.inject.Inject


class ForceUpdateChecker @Inject constructor(
    private val context: Context
) : UpdateChecker {

    override fun check(onUpdateNeededListener: (String) -> Unit) {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            val currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION)
            val appVersion = getAppVersion(context)
            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)

            if (!TextUtils.equals(currentVersion, appVersion)) {
                onUpdateNeededListener(updateUrl)
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
        val KEY_CURRENT_VERSION = "force_update_current_version"
        val KEY_UPDATE_URL = "force_update_store_url"

        fun with(context: Context): ForceUpdateChecker {
            return ForceUpdateChecker(context)
        }
    }
}