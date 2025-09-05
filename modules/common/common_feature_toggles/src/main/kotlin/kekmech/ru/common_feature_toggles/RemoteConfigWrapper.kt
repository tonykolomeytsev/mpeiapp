package kekmech.ru.common_feature_toggles

public interface RemoteConfigWrapper {

    public operator fun get(featureToggleKey: String): Boolean
}
