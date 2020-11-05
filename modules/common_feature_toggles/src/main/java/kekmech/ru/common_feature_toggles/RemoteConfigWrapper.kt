package kekmech.ru.common_feature_toggles

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

internal class RemoteConfigWrapper {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    operator fun get(featureToggleKey: String): Boolean = remoteConfig.getBoolean(featureToggleKey)
}