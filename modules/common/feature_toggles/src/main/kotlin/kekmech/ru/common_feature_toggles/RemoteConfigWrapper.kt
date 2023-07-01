package kekmech.ru.common_feature_toggles

interface RemoteConfigWrapper {

    fun getUntyped(name: String): String

    fun getAll(): Map<String, String>
}
