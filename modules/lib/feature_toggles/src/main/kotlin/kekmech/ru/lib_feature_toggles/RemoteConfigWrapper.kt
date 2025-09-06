package kekmech.ru.lib_feature_toggles

public interface RemoteConfigWrapper {

    public fun getUntyped(name: String): String

    public fun getAll(): Map<String, String>
}
