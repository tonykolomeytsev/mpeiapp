package kekmech.ru.mpeiapp

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.update.ForceUpdateChecker

object RemoteConfig {
    fun setup() {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = mutableMapOf<String, Any>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] =  false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] =  BuildConfig.VERSION_NAME
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] =       "https://play.google.com/store/apps/details?id=kekmech.ru.mpeiapp"

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults)
            .addOnSuccessListener {
                firebaseRemoteConfig.fetch(60) // fetch every minutes
                    .addOnSuccessListener { firebaseRemoteConfig.activate() } }
    }
}