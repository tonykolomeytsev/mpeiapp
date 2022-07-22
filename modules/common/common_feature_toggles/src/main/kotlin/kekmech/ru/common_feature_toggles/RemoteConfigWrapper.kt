package kekmech.ru.common_feature_toggles

interface RemoteConfigWrapper {

    operator fun get(featureToggleKey: String): Boolean
}
