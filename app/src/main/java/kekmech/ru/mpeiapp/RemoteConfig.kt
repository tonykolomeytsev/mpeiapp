package kekmech.ru.mpeiapp

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.mainscreen.ForceUpdateChecker

object RemoteConfig {
    fun setup() {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = mutableMapOf<String, Any>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] =  false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] =  BuildConfig.VERSION_NAME
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] =       "https://play.google.com/store/apps/details?id=kekmech.ru.mpeiapp"
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_DESCRIPTION] = ""

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseRemoteConfig.fetch(60) // fetch every minutes
                        .addOnCompleteListener{
                            if (it.isSuccessful) {
                                firebaseRemoteConfig.activate()
                            } else {
                                it.exception?.printStackTrace()
                            }
                        }
                } else {
                    it.exception?.printStackTrace()
                }
            }
    }
}