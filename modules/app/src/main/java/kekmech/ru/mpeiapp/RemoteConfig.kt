package kekmech.ru.mpeiapp

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.domain_force_update.ForceUpdateChecker

private const val REMOTE_CONFIG_FETCH_INTERVAL_SEC = 60L

object RemoteConfig {

    fun setup() {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = mutableMapOf<String, Any>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = BuildConfig.VERSION_NAME
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] =
            "https://play.google.com/store/apps/details?id=kekmech.ru.mpeiapp"
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_DESCRIPTION] = ""

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults)
            .addOnCompleteListener {
                if (it.isSuccessful) { // fetch every minute
                    firebaseRemoteConfig.fetch(REMOTE_CONFIG_FETCH_INTERVAL_SEC)
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                firebaseRemoteConfig.activate()
                            } else {
                                task.exception?.printStackTrace()
                            }
                        }
                } else {
                    it.exception?.printStackTrace()
                }
            }
    }
}